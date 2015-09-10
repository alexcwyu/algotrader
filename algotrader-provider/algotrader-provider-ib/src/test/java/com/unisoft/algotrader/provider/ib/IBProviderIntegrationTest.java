package com.unisoft.algotrader.provider.ib;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.unisoft.algotrader.config.SampleAppConfigModule;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.model.trading.TimeInForce;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.trading.OrderManager;
import com.unisoft.algotrader.utils.DateHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

import static com.unisoft.algotrader.model.refdata.Exchange.HKEX;
import static com.unisoft.algotrader.model.refdata.Exchange.IDEALPRO;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by alex on 9/1/15.
 */
public class IBProviderIntegrationTest {

    public static IBProvider provider;
    public static RefDataStore refDataStore;
    public static OrderManager orderManager;

    @BeforeClass
    public static void init(){
        Injector injector = Guice.createInjector(new SampleAppConfigModule());
        provider = injector.getInstance(IBProvider.class);
        refDataStore = injector.getInstance(RefDataStore.class);
        orderManager = injector.getInstance(OrderManager.class);
    }

    @Before
    public void setup(){
        provider.connect();
    }

    @After
    public void teardown(){
        provider.disconnect();
    }


    @Test
    public void testConnect(){
        assertTrue(provider.connected());
        assertTrue(provider.getIbSocket().getServerCurrentVersion() > 0);

        provider.disconnect();
        assertFalse(provider.connected());
    }

    @Test
    public void testSubscribeHistoricalMarketData() throws Exception{
        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("0005.HK", HKEX.getExchId());
        Date fromDate = DateHelper.fromYYYYMMDD(20150101);
        Date toDate = DateHelper.fromYYYYMMDD(20150901);
        HistoricalSubscriptionKey subscriptionKey = HistoricalSubscriptionKey.createDailySubscriptionKey(IBProvider.PROVIDER_ID, instrument.getInstId(), fromDate, toDate);

        assertTrue(provider.subscribeHistoricalData(subscriptionKey));
        Thread.sleep(5000);
        assertTrue(provider.unSubscribeHistoricalData(subscriptionKey.getSubscriptionId()));
        Thread.sleep(1000);
    }

    @Test
    public void testSubscribeRealtimeMarketData()throws Exception{
        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("EURUSD", IDEALPRO.getExchId());
        SubscriptionKey subscriptionKey = SubscriptionKey.createQuoteSubscriptionKey(IBProvider.PROVIDER_ID, instrument.getInstId());

        assertTrue(provider.subscribeRealTimeData(subscriptionKey));
        Thread.sleep(5000);
        assertTrue(provider.unSubscribeRealTimeData(subscriptionKey));
        Thread.sleep(1000);
    }


    @Test
    public void testSubmitOrder()throws Exception{
        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("EURUSD", IDEALPRO.getExchId());
        Order order = orderManager.newLimitOrder(instrument.getInstId(), "Test", IBProvider.PROVIDER_ID, Side.Buy, 1.1, 1000000, TimeInForce.Day);
        order.orderId = 12;
        provider.onNewOrderRequest(order);
        Thread.sleep(5000);

        provider.onOrderCancelRequest(order);
        Thread.sleep(5000);
    }


}
