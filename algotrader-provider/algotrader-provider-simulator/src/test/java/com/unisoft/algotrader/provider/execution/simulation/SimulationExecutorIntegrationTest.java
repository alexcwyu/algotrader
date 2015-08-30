package com.unisoft.algotrader.provider.execution.simulation;

import com.google.common.collect.Lists;
import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.event.SampleEventFactory;
import com.unisoft.algotrader.model.clock.SimulationClock;
import com.unisoft.algotrader.model.event.EventBusManager;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.OrdStatus;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.persistence.InMemoryTradingDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.trading.InstrumentDataManager;
import com.unisoft.algotrader.trading.OrderManager;
import com.unisoft.algotrader.trading.Strategy;
import com.unisoft.algotrader.trading.StrategyManager;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static com.unisoft.algotrader.provider.execution.simulation.SimulatorConfig.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

/**
 * Created by alex on 6/4/15.
 */
public class SimulationExecutorIntegrationTest {

    public static String mockStrategyId = "MockStrategy";
    public static long ordId = 0;
    public static Instrument testInstrument = SampleEventFactory.TEST_USD_INSTRUMENT;

    class MockStrategy extends Strategy{

        private final List<Order> orders = Lists.newArrayList();
        private final List<ExecutionReport> executionReports = Lists.newArrayList();
        private final OrderManager orderManager;

        public MockStrategy(OrderManager orderManager, TradingDataStore tradingDataStore, EventBusManager eventBusManager){
            super(mockStrategyId, tradingDataStore, eventBusManager);
            this.orderManager = orderManager;
        }

        @Override
        public void onExecutionReport(ExecutionReport executionReport) {
            executionReports.add(executionReport);

        }
//
//        @Override
//        public void onNewOrderRequest(Order order) {
//            orders.add(order);
//        }

        public void sendOrder(Order order){
            orderManager.onNewOrderRequest(order);
        }

    }

    private ProviderManager providerManager;
    private StrategyManager strategyManager;
    private OrderManager orderManager;
    private EventBusManager eventBusManager;
    private RingBuffer rb;
    private SimulationExecutor simulationExecutor;
    private InstrumentDataManager instrumentDataManager;
    private MockStrategy strategy;
    private TradingDataStore tradingDataStore;

    @BeforeClass
    public static void init(){
    }

    @Before
    public void setup(){

        providerManager = new ProviderManager();
        strategyManager = new StrategyManager();
        eventBusManager = new EventBusManager();
        orderManager = spy(new OrderManager(providerManager, strategyManager, eventBusManager));


        rb =RingBuffer.createSingleProducer(MarketDataContainer.FACTORY, 1024, new NoWaitStrategy());
        instrumentDataManager = new InstrumentDataManager(eventBusManager.marketDataRB);
        simulationExecutor = new SimulationExecutor(providerManager, orderManager, instrumentDataManager, new SimulationClock(), rb);
        simulationExecutor.config.fillOnQuote = true;
        simulationExecutor.config.fillOnQuoteMode = FillOnQuoteMode.LastQuote;
        simulationExecutor.config.fillOnTrade = true;
        simulationExecutor.config.fillOnTradeMode = FillOnTradeMode.LastTrade;
        simulationExecutor.config.fillOnBar = true;
        simulationExecutor.config.fillOnBarMode = FillOnBarMode.LastBarClose;

        tradingDataStore = new InMemoryTradingDataStore();

        strategy = new MockStrategy(orderManager, tradingDataStore, eventBusManager);
        strategyManager.register(strategy);

        instrumentDataManager.clear();

    }

    private void setFillMode(boolean fillOnQuote, FillOnQuoteMode fillOnQuoteMode,
                             boolean fillOnTrade, FillOnTradeMode fillOnTradeMode,
                             boolean fillOnBar, FillOnBarMode fillOnBarMode){

        simulationExecutor.config.fillOnQuote = fillOnQuote;
        simulationExecutor.config.fillOnQuoteMode = fillOnQuoteMode;
        simulationExecutor.config.fillOnTrade = fillOnTrade;
        simulationExecutor.config.fillOnTradeMode = fillOnTradeMode;
        simulationExecutor.config.fillOnBar = fillOnBar;
        simulationExecutor.config.fillOnBarMode = fillOnBarMode;

    }

    private void setMarketData(boolean setQuote, boolean setTrade, boolean setBar){
        if (setQuote)
            instrumentDataManager.onQuote(createQuote(89, 91, 1000, 1200));
        if (setTrade)
            instrumentDataManager.onTrade(createTrade(89.5, 500));
        if (setBar)
            instrumentDataManager.onBar(createBar(82, 95, 80, 92));
    }

    @Test
    public void should_not_fill_market_order_without_any_marketdata(){
        setMarketData(false, false, false);
        setFillMode(true, FillOnQuoteMode.LastQuote,
                true, FillOnTradeMode.LastTrade,
                true, FillOnBarMode.LastBarClose);

        Order order = createOrder(Side.Buy, OrdType.Market, 1000,0);
        strategy.sendOrder(order);

        assertTrue(strategy.executionReports.size() == 1);

        ExecutionReport report = strategy.executionReports.get(0);

        assertEquals(OrdStatus.New, report.ordStatus);
        assertEquals(0.0, report.filledQty, 0.0);
    }


    @Test
    public void should_fill_market_order_on_last_quote(){
        setMarketData(true, true, true);
        setFillMode(true, FillOnQuoteMode.LastQuote,
                false, FillOnTradeMode.LastTrade,
                false, FillOnBarMode.LastBarClose);

        Order order = createOrder(Side.Buy, OrdType.Market, 1000,0);
        strategy.sendOrder(order);
        assertTrue(strategy.executionReports.size() == 2);
        assertNotFilled(strategy.executionReports.get(0));
        ExecutionReport report = strategy.executionReports.get(1);
        assertEquals(1000, report.filledQty, 0.0);
        assertEquals(91, report.avgPrice, 0.0);


        Order order2 = createOrder(Side.Sell, OrdType.Market, 900,0);
        strategy.sendOrder(order2);
        assertTrue(strategy.executionReports.size() == 4);
        assertNotFilled(strategy.executionReports.get(2));
        ExecutionReport report2 = strategy.executionReports.get(3);
        assertEquals(900, report2.filledQty, 0.0);
        assertEquals(89, report2.avgPrice, 0.0);

    }


    @Test
    public void should_fill_market_order_on_next_quote(){
        setMarketData(true, true, true);
        setFillMode(true, FillOnQuoteMode.NextQuote,
                true, FillOnTradeMode.NextTrade,
                true, FillOnBarMode.NextBarClose);

        Order order = createOrder(Side.Buy, OrdType.Market, 1000,0);
        strategy.sendOrder(order);

        assertTrue(strategy.executionReports.size() == 1);

        assertNotFilled(strategy.executionReports.get(0));

        simulationExecutor.onQuote(createQuote(89, 90, 1000, 1200));

        assertTrue(strategy.executionReports.size() == 2);

        ExecutionReport report = strategy.executionReports.get(1);

        assertEquals(1000, report.filledQty, 0.0);
        assertEquals(90, report.avgPrice, 0.0);
    }


    @Test
    public void should_fill_market_order_on_last_trade(){
        setMarketData(true, true, true);
        setFillMode(false, FillOnQuoteMode.NextQuote,
                true, FillOnTradeMode.LastTrade,
                true, FillOnBarMode.LastBarClose);


        Order order = createOrder(Side.Buy, OrdType.Market, 1000,0);
        strategy.sendOrder(order);
        assertTrue(strategy.executionReports.size() == 2);
        assertNotFilled(strategy.executionReports.get(0));
        ExecutionReport report = strategy.executionReports.get(1);
        assertEquals(1000, report.filledQty, 0.0);
        assertEquals(89.5, report.avgPrice, 0.0);


        Order order2 = createOrder(Side.Sell, OrdType.Market, 900,0);
        strategy.sendOrder(order2);
        assertTrue(strategy.executionReports.size() == 4);
        assertNotFilled(strategy.executionReports.get(2));
        ExecutionReport report2 = strategy.executionReports.get(3);
        assertEquals(900, report2.filledQty, 0.0);
        assertEquals(89.5, report2.avgPrice, 0.0);
    }

    @Test
    public void should_fill_market_order_on_next_trade(){

        setMarketData(true, true, true);
        setFillMode(false, FillOnQuoteMode.NextQuote,
                true, FillOnTradeMode.NextTrade,
                true, FillOnBarMode.NextBarClose);

        Order order = createOrder(Side.Buy, OrdType.Market, 1000,0);
        strategy.sendOrder(order);

        assertTrue(strategy.executionReports.size() == 1);
        assertNotFilled(strategy.executionReports.get(0));

        simulationExecutor.onTrade(createTrade(95, 1000));

        assertTrue(strategy.executionReports.size() == 2);
        ExecutionReport report = strategy.executionReports.get(1);
        assertEquals(1000, report.filledQty, 0.0);
        assertEquals(95, report.avgPrice, 0.0);
    }

    @Test
    public void should_fill_market_order_on_last_bar_close(){


        setMarketData(true, true, true);
        setFillMode(false, FillOnQuoteMode.NextQuote,
                false, FillOnTradeMode.NextTrade,
                true, FillOnBarMode.LastBarClose);

        Order order = createOrder(Side.Buy, OrdType.Market, 1000,0);
        strategy.sendOrder(order);
        assertTrue(strategy.executionReports.size() == 2);
        assertNotFilled(strategy.executionReports.get(0));
        ExecutionReport report = strategy.executionReports.get(1);
        assertEquals(1000, report.filledQty, 0.0);
        assertEquals(92, report.avgPrice, 0.0);


        Order order2 = createOrder(Side.Sell, OrdType.Market, 900,0);
        strategy.sendOrder(order2);
        assertTrue(strategy.executionReports.size() == 4);
        assertNotFilled(strategy.executionReports.get(2));
        ExecutionReport report2 = strategy.executionReports.get(3);
        assertEquals(900, report2.filledQty, 0.0);
        assertEquals(92, report2.avgPrice, 0.0);
    }

    @Test
    public void should_fill_market_order_on_next_bar_open(){
        setMarketData(true, true, true);
        setFillMode(false, FillOnQuoteMode.NextQuote,
                false, FillOnTradeMode.NextTrade,
                true, FillOnBarMode.NextBarOpen);


        Order order = createOrder(Side.Buy, OrdType.Market, 1000,0);
        strategy.sendOrder(order);

        assertTrue(strategy.executionReports.size() == 1);
        assertNotFilled(strategy.executionReports.get(0));

        simulationExecutor.onBar(createBar(500, 987, 87, 600));

        assertTrue(strategy.executionReports.size() == 2);
        ExecutionReport report = strategy.executionReports.get(1);
        assertEquals(1000, report.filledQty, 0.0);
        assertEquals(500, report.avgPrice, 0.0);
    }

    @Test
    public void should_fill_market_order_on_next_bar_close(){

    }

    @Test
    public void should_partialfill(){

    }

    public void should_fill_limited_order_immediately(){

    }

    public void should_fill_limited_order_on_next_marketdata(){

    }

    private void assertNotFilled(ExecutionReport report){
        assertEquals(OrdStatus.New, report.ordStatus);
        assertEquals(0.0, report.filledQty, 0.0);
        assertEquals(0.0, report.avgPrice, 0.0);
    }


    private Order createOrder(Side side, OrdType type, double qty, double price){
        Order order = new Order();
        order.orderId = ordId++;
        order.instId = testInstrument.getInstId();
        order.strategyId = mockStrategyId;
        order.execProviderId = simulationExecutor.providerId();
        order.side= side;
        order.ordType = type;
        order.ordQty=qty;
        order.limitPrice = price;
        return order;
    }

    private Quote createQuote(double bid,
                              double ask,
                              int bidSize,
                              int askSize){
        return new Quote(testInstrument.getInstId(), System.currentTimeMillis(),
                bid,ask,bidSize,askSize);
    }

    private Trade createTrade(double price,
                              int size){
        return new Trade(testInstrument.getInstId(), System.currentTimeMillis(), price,size);
    }

    private Bar createBar(
            double open,
            double high,
            double low,
            double close){
        return new Bar(testInstrument.getInstId(), 60, System.currentTimeMillis(), open, high, low, close, 0, 0);
    }
}
