package com.unisoft.algotrader.provider.ib;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.unisoft.algotrader.config.SampleAppConfigModule;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.model.trading.TimeInForce;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.provider.ib.api.event.DefaultIBEventHandler;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.order.OrderStatus;
import com.unisoft.algotrader.trading.OrderManager;
import com.unisoft.algotrader.utils.DateHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Date;
import java.util.List;

import static com.unisoft.algotrader.model.refdata.Exchange.HKEX;
import static com.unisoft.algotrader.model.refdata.Exchange.IDEALPRO;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.*;

/**
 * Created by alex on 9/1/15.
 */
public class IBSocketIntegrationTest {

    public IBSocket socket;
    public IBEventHandler eventHandler;
    public static IBConfig ibConfig;
    public static RefDataStore refDataStore;
    public static OrderManager orderManager;
    public static long nextRequestId = 1;
    @BeforeClass
    public static void init(){
        Injector injector = Guice.createInjector(new SampleAppConfigModule());
        ibConfig = injector.getInstance(IBConfig.class);
        refDataStore = injector.getInstance(RefDataStore.class);
        orderManager = injector.getInstance(OrderManager.class);
    }

    @Before
    public void setup(){
        eventHandler = spy(new DefaultIBEventHandler());
        socket = new IBSocket(ibConfig, eventHandler, refDataStore);
        socket.connect();
    }

    @After
    public void teardown(){
        socket.disconnect();
    }

    @Test
    public void testConnect(){
        assertTrue(socket.isConnected());
        assertTrue(socket.getServerCurrentVersion() > 0);
        socket.disconnect();
        assertFalse(socket.isConnected());
    }

    @Test
    public void testHistoricalMarketDataSubscription(){
        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("0005.HK", HKEX.getExchId());
        Date fromDate = DateHelper.fromYYYYMMDD(20150101);
        Date toDate = DateHelper.fromYYYYMMDD(20150901);
        HistoricalSubscriptionKey subscriptionKey = HistoricalSubscriptionKey.createDailySubscriptionKey(IBProvider.PROVIDER_ID, instrument.getInstId(), fromDate, toDate);

        long requestId = nextRequestId ++;
        socket.subscribeHistoricalData(requestId, subscriptionKey);
        ArgumentCaptor<List<Bar>> captor = ArgumentCaptor.forClass(List.class);
        verify(eventHandler, timeout(2000).atLeastOnce()).onHistoricalDataListEvent(anyInt(), captor.capture());
        assertTrue(captor.getValue().size() > 0);

        socket.unsubscribeHistoricalData(requestId);
    }

    @Test
    public void testRealtimeMarketDataSubscription(){
        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("EURUSD", IDEALPRO.getExchId());
        SubscriptionKey subscriptionKey = SubscriptionKey.createQuoteSubscriptionKey(IBProvider.PROVIDER_ID, instrument.getInstId());

        long requestId = nextRequestId ++;
        socket.subscribeRealTimeData(requestId, subscriptionKey);
        verify(eventHandler, timeout(10000).atLeastOnce()).onRealTimeBarEvent(anyInt(), anyLong(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyLong(), anyDouble(), anyInt());

        socket.unsubscribeRealTimeData(requestId);
    }

    @Test
    public void testOrderSubmission(){
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(eventHandler, timeout(1000).atLeastOnce()).onNextValidOrderIdEvent(captor.capture());
        assertTrue(captor.getValue() > 0);

        int orderId = captor.getValue();

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("EURUSD", IDEALPRO.getExchId());
        Order order = orderManager.newLimitOrder(instrument.getInstId(), "Test", IBProvider.PROVIDER_ID, Side.Buy, 1.1, 1000000, TimeInForce.Day);
        order.extOrderId = orderId;
        socket.placeOrder(order);
        ArgumentCaptor<Long> captor2 = ArgumentCaptor.forClass(Long.class);
        verify(eventHandler, timeout(5000).atLeastOnce()).onRetrieveOpenOrderEvent(
                captor2.capture(), any(), any(), any());
        assertEquals(orderId, captor2.getValue().intValue());

        ArgumentCaptor<Integer> captor3 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<OrderStatus> captor4 = ArgumentCaptor.forClass(OrderStatus.class);
        verify(eventHandler, timeout(2000).atLeastOnce()).onOrderStatusUpdateEvent(
                captor3.capture(), captor4.capture(), anyInt(),
                anyInt(), anyDouble(), anyInt(),
                anyInt(), anyDouble(), anyInt(), isNull(String.class));
        assertEquals(orderId, captor3.getValue().intValue());
        assertEquals(OrderStatus.SUBMITTED, captor4.getValue());

        reset(eventHandler);

        socket.cancelOrder(order.extOrderId);
        ArgumentCaptor<Integer> captor5 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<OrderStatus> captor6 = ArgumentCaptor.forClass(OrderStatus.class);
        verify(eventHandler, timeout(2000).atLeastOnce()).onOrderStatusUpdateEvent(
                captor5.capture(), captor6.capture(), anyInt(),
                anyInt(), anyDouble(), anyInt(),
                anyInt(), anyDouble(), anyInt(), isNull(String.class));
        assertEquals(orderId, captor5.getValue().intValue());
        assertEquals(OrderStatus.CANCELLED, captor6.getValue());
    }



}
