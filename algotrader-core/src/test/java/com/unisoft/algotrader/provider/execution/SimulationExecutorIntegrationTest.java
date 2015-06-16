package com.unisoft.algotrader.provider.execution;

import com.google.common.collect.Lists;
import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.core.*;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.MarketDataContainer;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.event.execution.ExecutionReport;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.order.OrderManager;
import com.unisoft.algotrader.provider.data.InstrumentDataManager;
import com.unisoft.algotrader.strategy.Strategy;
import com.unisoft.algotrader.strategy.StrategyManager;
import com.unisoft.algotrader.threading.disruptor.waitstrategy.NoWaitStrategy;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static com.unisoft.algotrader.provider.execution.SimulatorConfig.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by alex on 6/4/15.
 */
public class SimulationExecutorIntegrationTest {

    public static String mockStrategyId = "MockStrategy";
    public static long ordId = 0;
    public static Instrument testInstrument = new Instrument("TestInst", "TestExch", Currency.HKD.ccyId);

    class MockStrategy extends Strategy{

        private final List<Order> orders = Lists.newArrayList();
        private final List<ExecutionReport> executionReports = Lists.newArrayList();
        private final OrderManager orderManager;

        public MockStrategy(OrderManager orderManager){
            super(mockStrategyId);
            this.orderManager = orderManager;
        }

        @Override
        public void onExecutionReport(ExecutionReport executionReport) {
            executionReports.add(executionReport);

        }

        @Override
        public void onOrder(Order order) {
            orders.add(order);
        }

        public void sendOrder(Order order){
            orderManager.onOrder(order);
        }

    }

    private OrderManager orderManager;
    private RingBuffer rb;
    private SimulationExecutor simulationExecutor;

    private MockStrategy strategy;

    @BeforeClass
    public static void init(){
        InstrumentManager.INSTANCE.add(testInstrument);
    }

    @Before
    public void setup(){
        orderManager = spy(new OrderManager());

        rb =RingBuffer.createSingleProducer(MarketDataContainer.FACTORY, 1024, new NoWaitStrategy());

        simulationExecutor = new SimulationExecutor(orderManager, InstrumentDataManager.INSTANCE, rb);
        simulationExecutor.config.fillOnQuote = true;
        simulationExecutor.config.fillOnQuoteMode = FillOnQuoteMode.LastQuote;
        simulationExecutor.config.fillOnTrade = true;
        simulationExecutor.config.fillOnTradeMode = FillOnTradeMode.LastTrade;
        simulationExecutor.config.fillOnBar = true;
        simulationExecutor.config.fillOnBarMode = FillOnBarMode.LastBarClose;

        strategy = new MockStrategy(orderManager);
        StrategyManager.INSTANCE.register(strategy);

        InstrumentDataManager.INSTANCE.clear();

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
            InstrumentDataManager.INSTANCE.onQuote(createQuote(89, 91, 1000, 1200));
        if (setTrade)
            InstrumentDataManager.INSTANCE.onTrade(createTrade(89.5, 500));
        if (setBar)
            InstrumentDataManager.INSTANCE.onBar(createBar(95, 80, 82, 92));
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
        assertEquals(91, report.avgPx, 0.0);


        Order order2 = createOrder(Side.Sell, OrdType.Market, 900,0);
        strategy.sendOrder(order2);
        assertTrue(strategy.executionReports.size() == 4);
        assertNotFilled(strategy.executionReports.get(2));
        ExecutionReport report2 = strategy.executionReports.get(3);
        assertEquals(900, report2.filledQty, 0.0);
        assertEquals(89, report2.avgPx, 0.0);

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
        assertEquals(90, report.avgPx, 0.0);
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
        assertEquals(89.5, report.avgPx, 0.0);


        Order order2 = createOrder(Side.Sell, OrdType.Market, 900,0);
        strategy.sendOrder(order2);
        assertTrue(strategy.executionReports.size() == 4);
        assertNotFilled(strategy.executionReports.get(2));
        ExecutionReport report2 = strategy.executionReports.get(3);
        assertEquals(900, report2.filledQty, 0.0);
        assertEquals(89.5, report2.avgPx, 0.0);
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
        assertEquals(95, report.avgPx, 0.0);
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
        assertEquals(92, report.avgPx, 0.0);


        Order order2 = createOrder(Side.Sell, OrdType.Market, 900,0);
        strategy.sendOrder(order2);
        assertTrue(strategy.executionReports.size() == 4);
        assertNotFilled(strategy.executionReports.get(2));
        ExecutionReport report2 = strategy.executionReports.get(3);
        assertEquals(900, report2.filledQty, 0.0);
        assertEquals(92, report2.avgPx, 0.0);
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

        simulationExecutor.onBar(createBar(987, 87, 500, 600));

        assertTrue(strategy.executionReports.size() == 2);
        ExecutionReport report = strategy.executionReports.get(1);
        assertEquals(1000, report.filledQty, 0.0);
        assertEquals(500, report.avgPx, 0.0);
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
        assertEquals(0.0, report.avgPx, 0.0);
    }


    private Order createOrder(Side side, OrdType type, double qty, double price){
        Order order = new Order();
        order.orderId = ordId++;
        order.instId = testInstrument.instId;
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
        return new Quote(testInstrument.instId, System.currentTimeMillis(),
                bid,ask,bidSize,askSize);
    }

    private Trade createTrade(double price,
                              int size){
        return new Trade(testInstrument.instId, System.currentTimeMillis(), price,size);
    }

    private Bar createBar(
            double high,
            double low,
            double open,
            double close){
        return new Bar(testInstrument.instId, System.currentTimeMillis(), 60, high, low, open, close, 0, 0);
    }
}
