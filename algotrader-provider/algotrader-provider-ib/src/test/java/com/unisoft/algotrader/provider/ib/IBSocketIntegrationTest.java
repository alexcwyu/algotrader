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
import com.unisoft.algotrader.provider.data.MarketDepthSubscriptionKey;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.provider.ib.api.event.DefaultIBEventHandler;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.data.MarketDataType;
import com.unisoft.algotrader.provider.ib.api.model.data.MarketScannerFilter;
import com.unisoft.algotrader.provider.ib.api.model.data.ReportType;
import com.unisoft.algotrader.provider.ib.api.model.execution.ExecutionReportFilter;
import com.unisoft.algotrader.provider.ib.api.model.fa.FinancialAdvisorDataType;
import com.unisoft.algotrader.provider.ib.api.model.order.ExerciseAction;
import com.unisoft.algotrader.provider.ib.api.model.order.OrderStatus;
import com.unisoft.algotrader.provider.ib.api.model.system.IBModelUtils;
import com.unisoft.algotrader.provider.ib.api.model.system.LogLevel;
import com.unisoft.algotrader.trading.OrderManager;
import com.unisoft.algotrader.utils.DateHelper;
import org.junit.*;
import org.mockito.ArgumentCaptor;

import java.util.Date;
import java.util.List;

import static com.unisoft.algotrader.model.refdata.Exchange.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
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
        socket.connect(false);
    }

    @After
    public void teardown()throws Exception{
        Thread.sleep(500);
        socket.disconnect();
        Thread.sleep(500);
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
    public void testMarketDataSubscription(){
        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("EURUSD", IDEALPRO.getExchId());
        SubscriptionKey subscriptionKey = SubscriptionKey.createQuoteSubscriptionKey(IBProvider.PROVIDER_ID, instrument.getInstId());

        long requestId = nextRequestId ++;
        socket.subscribeMarketData(requestId, subscriptionKey, true);
        verify(eventHandler, timeout(5000).atLeastOnce()).onTickPriceEvent(anyInt(), any(), anyDouble(), anyBoolean());

        socket.unsubscribeMarketData(requestId);
    }

    @Test
    public void testMarketDepthSubscription(){
        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("EURUSD", IDEALPRO.getExchId());
        MarketDepthSubscriptionKey subscriptionKey = MarketDepthSubscriptionKey.createSubscriptionKey(IBProvider.PROVIDER_ID, instrument.getInstId(), 1);
        long requestId = nextRequestId ++;

        socket.subscribeMarketDepth(requestId, subscriptionKey);
        verify(eventHandler, timeout(5000).atLeastOnce()).onMarketDepthUpdateEvent(anyInt(), anyInt(), any(), any(), anyDouble(), anyInt());

        socket.unsubscribeMarketDepth(requestId);
    }

    @Ignore
    public void testNewsBulletinSubscription(){
        socket.subscribeNewsBulletin(false);

        verify(eventHandler, timeout(5000).atLeastOnce()).onNewsBulletinUpdateEvent(anyInt(), any(), any(), any());

        socket.unsubscribeNewsBulletin();
    }

    @Test
    public void testAccountSummarySubscription(){
        long requestId = nextRequestId++;
        socket.subscribeAccountSummary(requestId, "All", "AccountType");

        verify(eventHandler, timeout(5000).atLeastOnce()).onAccountSummaryEvent(eq((int) requestId), any(), eq("AccountType"), any(), any());

        socket.unsubscribeAccountSummary(requestId);
    }

    @Test
    public void testGroupEventSubscription(){
        long requestId = nextRequestId++;
        socket.subscribeGroupEvents(requestId, 1);

        verify(eventHandler, timeout(5000).atLeastOnce()).onDisplayGroupUpdatedEvent(eq((int) requestId), any());

        socket.unsubscribeGroupEvents(requestId);
    }


    @Ignore
    public void testFundamentalDataSubscription(){
        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("IBM", NYSE.getExchId());

        long requestId = nextRequestId ++;
        socket.subscribeFundamentalData(requestId, ReportType.SUMMARY, instrument);

        verify(eventHandler, timeout(5000).atLeastOnce()).onFundamentalDataEvent(anyInt(), any());

        socket.unsubscribeFundamentalData(requestId);
    }

    @Ignore
    public void testScannerSubscription(){
        long requestId = nextRequestId ++;

        MarketScannerFilter filter = new MarketScannerFilter();
        filter.setInstrument("STK");
        filter.setLocationCode("STK.US.MAJOR");
        filter.setNumberOfRows(10);
        filter.setScannerCode("HIGH_OPT_VOLUME_PUT_CALL_RATIO");
        filter.setAboveVolume(0);
        filter.setAbovePrice(3);
        filter.setAboveMarketCapitalization(100000000);

//        filter.setAboveVolume(5000000);
        socket.subscribeScanner(requestId, filter);

        verify(eventHandler, timeout(5000).atLeastOnce()).onMarketScannerDataListEvent(eq((int) requestId), any());

        socket.unsubscribeScanner(requestId);
    }


    @Ignore
    public void testOptionImpliedVolatilitySubscription(){
        long requestId = nextRequestId ++;
        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("EUR", GLOBEX.getExchId());
        instrument.setPutCall(Instrument.PutCall.Call);
        instrument.setExpiryDate(IBModelUtils.convertDate("20160314"));
        socket.subscribeOptionImpliedVolatility(requestId, instrument, 1.0, 1.0);
        verify(eventHandler, timeout(1000).atLeastOnce()).onTickOptionComputationEvent(anyInt(), any(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble());
        socket.unsubscribeOptionImpliedVolatility(requestId);
    }


    @Ignore
    public void testOptionPriceSubscription(){
        long requestId = nextRequestId ++;
        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("EUR", GLOBEX.getExchId());
        instrument.setPutCall(Instrument.PutCall.Call);
        instrument.setExpiryDate(IBModelUtils.convertDate("20160314"));
        socket.subscribeOptionPrice(requestId, instrument, 1.0, 1.0);
        verify(eventHandler, timeout(1000).atLeastOnce()).onTickOptionComputationEvent(anyInt(), any(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble());
        socket.unsubscribeOptionPrice(requestId);
    }



    @Ignore
    public void testExerciseOptions(){
        long requestId = nextRequestId ++;
        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("EUR", GLOBEX.getExchId());
        instrument.setPutCall(Instrument.PutCall.Call);
        instrument.setExpiryDate(IBModelUtils.convertDate("20160314"));
        socket.exerciseOptions(requestId, instrument, ExerciseAction.EXERCISE, 1000, null, false);
        verify(eventHandler, timeout(5000).atLeastOnce()).onTickOptionComputationEvent(anyInt(), any(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    public void testRequestManagedAccounts(){
        socket.requestManagedAccounts();
        verify(eventHandler, timeout(5000).atLeastOnce()).onManagedAccountListEvent(anyString());
    }


    @Ignore // non FA customer
    public void testRequestReplaceFA(){
        socket.requestFA(FinancialAdvisorDataType.ACCOUNT_ALIASES);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(eventHandler, timeout(5000).atLeastOnce()).onFinancialAdvisorConfigurationEvent(eq(FinancialAdvisorDataType.ACCOUNT_ALIASES), captor.capture());

        socket.replaceFA(FinancialAdvisorDataType.ACCOUNT_ALIASES, captor.getValue());
    }


    @Test
    public void testRequestAccountUpdates(){
        socket.requestAccountUpdates(null);
        verify(eventHandler, timeout(5000).atLeastOnce()).onAccountUpdateValueEvent(any(), any(), any(), any());
        verify(eventHandler, timeout(5000).atLeastOnce()).onAccountUpdateTimeEvent(anyString());
        verify(eventHandler, timeout(5000).atLeastOnce()).onAccountUpdateValueEndEvent(anyString());
    }

    @Test
    public void testRequestCurrentTime() {
        socket.requestCurrentTime();
        verify(eventHandler, timeout(5000).atLeastOnce()).onServerCurrentTimeEvent(anyLong());
    }

    @Test
    public void testRequestNextValidOrderId() {
        socket.requestNextValidOrderId();
        verify(eventHandler, timeout(5000).atLeastOnce()).onNextValidOrderIdEvent(anyInt());

        socket.requestNextValidOrderId(10);
        verify(eventHandler, timeout(5000).atLeastOnce()).onNextValidOrderIdEvent(anyInt());
    }


    @Test
    public void testRequestScannerParameters(){
        socket.requestScannerParameters();
        verify(eventHandler, timeout(5000).atLeastOnce()).onMarketScannerValidParametersEvent(anyString());
    }

    @Test
    public void testRequestAllOpenOrders(){
        socket.requestAllOpenOrders();
        verify(eventHandler, timeout(5000).atLeastOnce()).onRetrieveOpenOrderEndEvent();
    }

    @Test
    public void testRequestOpenOrders(){
        socket.requestOpenOrders();
        verify(eventHandler, timeout(5000).atLeastOnce()).onRetrieveOpenOrderEndEvent();
    }


    @Ignore
    public void testRequestMarketDataType(){
        socket.requestMarketDataType(MarketDataType.FROZEN);
        verify(eventHandler, timeout(5000).atLeastOnce()).onMarketDataTypeEvent(anyInt(), any());
    }

    @Test
    public void testRequestAutoOpenOrders(){
        socket.requestAutoOpenOrders(true);
        //verify(eventHandler, timeout(5000).atLeastOnce()).onMarketDataTypeEvent(anyInt(), any());
    }

    @Test
    public void testRequestExecutions(){
        long requestId = nextRequestId ++;
        ExecutionReportFilter executionReportFilter = new ExecutionReportFilter();
        socket.requestExecutions(requestId, executionReportFilter);
        verify(eventHandler, timeout(5000).atLeastOnce()).onExecutionReportEndEvent(eq((int) requestId));
    }


    @Test
    public void testRequestContractSpecification(){
        long requestId = nextRequestId ++;
        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("EURUSD", IDEALPRO.getExchId());
        socket.requestContractSpecification(requestId, instrument);
        verify(eventHandler, timeout(5000).atLeastOnce()).onInstrumentSpecificationEvent(eq((int) requestId), any());
    }


    @Test
    public void testQueryDisplayGroups(){
        long requestId = nextRequestId ++;
        socket.queryDisplayGroups(requestId);
        verify(eventHandler, timeout(5000).atLeastOnce()).onDisplayGroupListEvent(eq((int) requestId), any());
    }

    @Test
    public void testSetServerLogLevel(){
        socket.setServerLogLevel(LogLevel.INFO);
    }


    @Ignore
    public void testVerifyMessage(){
        socket.verifyMessage("test");
    }

    @Ignore
    public void verifyRequest(){
        socket.verifyRequest("test", "71");
    }

    @Ignore
    public void testUpdateDisplayGroups(){
        long requestId = nextRequestId ++;
        socket.updateDisplayGroups(requestId, "8314@ARCA");
        verify(eventHandler, timeout(5000).atLeastOnce()).onDisplayGroupUpdatedEvent(eq((int) requestId), any());
    }


    @Test
    public void testPlaceUpdateAndCancelOrder()throws Exception{
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(eventHandler, timeout(1000).atLeastOnce()).onNextValidOrderIdEvent(captor.capture());
        assertTrue(captor.getValue() > 0);

        int orderId = captor.getValue();

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("EURUSD", IDEALPRO.getExchId());
        Order order = orderManager.newLimitOrder(instrument.getInstId(), "Test", IBProvider.PROVIDER_ID, Side.Buy, 1.1, 1000000, TimeInForce.Day);
        order.orderId = orderId;
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

        Thread.sleep(1000);
        order.limitPrice(1.0);
        socket.placeOrder(order);

        Thread.sleep(1000);
        verify(eventHandler, timeout(5000).atLeastOnce()).onRetrieveOpenOrderEvent(
                captor2.capture(), any(), any(), any());
        assertEquals(orderId, captor2.getValue().intValue());

        verify(eventHandler, timeout(2000).atLeastOnce()).onOrderStatusUpdateEvent(
                captor3.capture(), captor4.capture(), anyInt(),
                anyInt(), anyDouble(), anyInt(),
                anyInt(), anyDouble(), anyInt(), isNull(String.class));
        assertEquals(orderId, captor3.getValue().intValue());
        assertEquals(OrderStatus.SUBMITTED, captor4.getValue());
        reset(eventHandler);


        socket.cancelOrder(order.orderId);
        ArgumentCaptor<Integer> captor5 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<OrderStatus> captor6 = ArgumentCaptor.forClass(OrderStatus.class);
        verify(eventHandler, timeout(2000).atLeastOnce()).onOrderStatusUpdateEvent(
                captor5.capture(), captor6.capture(), anyInt(),
                anyInt(), anyDouble(), anyInt(),
                anyInt(), anyDouble(), anyInt(), isNull(String.class));
        assertEquals(orderId, captor5.getValue().intValue());
        assertEquals(OrderStatus.CANCELLED, captor6.getValue());
    }


    @Ignore
    public void testAccountPositionsUpdate(){
        socket.requestPositions();
        verify(eventHandler, timeout(1000).atLeastOnce()).onPositionEvent(any(), any(), anyInt(), anyDouble());
        verify(eventHandler, timeout(1000).atLeastOnce()).onPositionEndEvent();
        socket.cancelPositions();
    }



    @Test
    public void testCancelAllOrders(){
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(eventHandler, timeout(1000).atLeastOnce()).onNextValidOrderIdEvent(captor.capture());
        assertTrue(captor.getValue() > 0);

        int orderId = captor.getValue();

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange("EURUSD", IDEALPRO.getExchId());
        Order order = orderManager.newLimitOrder(instrument.getInstId(), "Test", IBProvider.PROVIDER_ID, Side.Buy, 1.1, 1000000, TimeInForce.Day);
        order.orderId = orderId;
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


        socket.cancelAllOrders();
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
