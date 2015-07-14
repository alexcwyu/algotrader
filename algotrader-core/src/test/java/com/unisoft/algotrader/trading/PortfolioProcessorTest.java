package com.unisoft.algotrader.trading;

import com.unisoft.algotrader.model.clock.Clock;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.*;
import com.unisoft.algotrader.persistence.SampleInMemoryRefDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.refdata.InstrumentManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 5/29/15.
 */
public class PortfolioProcessorTest {

    private static final Logger LOG = LogManager.getLogger(PortfolioProcessorTest.class);

    public static long ordId = 0;
    public static long execId = 100;
    public static String portfolioId = "TestPortfolio";
    private static Instrument instrument1 = InstrumentManager.INSTANCE.createStock("0005.HK", "HKEX", "HKD");
    private static Instrument instrument2 = InstrumentManager.INSTANCE.createStock("0959.HK", "HKEX", "HKD");
    private static Instrument instrument3 = InstrumentManager.INSTANCE.createStock("2628.HK", "HKEX", "HKD");

    private Account account;
    private Portfolio portfolio;
    private PortfolioProcessor portfolioProcessor;

    @BeforeClass
    public static void init() {
        InstrumentManager.INSTANCE.add(instrument1);
        InstrumentManager.INSTANCE.add(instrument2);
        InstrumentManager.INSTANCE.add(instrument3);
    }

    @Before
    public void setup() {
        account = new Account("Test", "Testing Account", Currency.HKD, 1000000);
        portfolio = new Portfolio(portfolioId, account.getAccountId());
        portfolioProcessor = new PortfolioProcessor(portfolio, account, new SampleInMemoryRefDataStore(), Clock.CLOCK);
    }

    @Test
    public void test_value(){
        portfolioProcessor = spy(portfolioProcessor);

        when(portfolioProcessor.accountValue()).thenReturn(999.0);
        when(portfolioProcessor.positionValue()).thenReturn(10000.0);
        assertEquals(10999.0, portfolioProcessor.value(), 0.0);

        when(portfolioProcessor.accountValue()).thenReturn(888.0);
        when(portfolioProcessor.positionValue()).thenReturn(60000.0);
        assertEquals(60888, portfolioProcessor.value(), 0.0);
    }


    @Test
    public void test_account_value(){
        assertEquals(1_000_000, portfolioProcessor.accountValue(), 0.0);
        assertEquals(portfolioProcessor.value(), portfolioProcessor.accountValue(), 0.0);

        account.deposit(System.currentTimeMillis(), Currency.HKD, 1000, "");
        assertEquals(1_001_000, portfolioProcessor.accountValue(), 0.0);
        assertEquals(portfolioProcessor.value(), portfolioProcessor.accountValue(), 0.0);

    }

    @Test
    public void test_position_value(){

        assertEquals(0.0, portfolioProcessor.positionValue(), 0.0);

        Order order = limitOrder(instrument1.getInstId(), 10000, 2, Side.Buy);
        ExecutionReport er = executionReport(order);
        order.add(er);
        portfolioProcessor.add(order);

        //10000 * 2
        assertEquals(20000, portfolioProcessor.positionValue(), 0.0);

        order = limitOrder(instrument1.getInstId(), 10000, 1, Side.Buy);
        er = executionReport(order);
        order.add(er);
        portfolioProcessor.add(order);

        //10000 * 3
        assertEquals(30000, portfolioProcessor.positionValue(), 0.0);

        order = limitOrder(instrument1.getInstId(), 18000, 1, Side.Buy);
        er = executionReport(order);
        order.add(er);
        portfolioProcessor.add(order);

        //18000 * 4
        assertEquals(72000, portfolioProcessor.positionValue(), 0.0);

        order = limitOrder(instrument1.getInstId(), 20000, 6, Side.Sell);
        er = executionReport(order);
        order.add(er);
        portfolioProcessor.add(order);

        //-20000 * 2
        assertEquals(-40000, portfolioProcessor.positionValue(), 0.0);

        order = limitOrder(instrument2.getInstId(), 2000, 50, Side.Buy);
        er = executionReport(order);
        order.add(er);
        portfolioProcessor.add(order);
        //(-20000 * 2) + 2000 * 50
        assertEquals(60000, portfolioProcessor.positionValue(), 0.0);

        order = limitOrder(instrument3.getInstId(), 85, 10, Side.Buy);
        er = executionReport(order);
        order.add(er);
        portfolioProcessor.add(order);

        //(-20000 * 2) + 2000 * 50 + 85 * 10
        assertEquals(60850, portfolioProcessor.positionValue(), 0.0);


        //(-20000 * 2) + 2000 * 50 + 88 * 10
        portfolioProcessor.onTrade(new Trade(instrument3.getInstId(), System.currentTimeMillis(), 88, 1));
        assertEquals(60880, portfolioProcessor.positionValue(), 0.0);

        //(-21000 * 2) + 2000 * 50 + 88 * 10
        portfolioProcessor.onQuote(new Quote(instrument1.getInstId(), System.currentTimeMillis(), 19000, 21000, 1, 1));
        assertEquals(58880, portfolioProcessor.positionValue(), 0.0);

        //(-21000 * 2) + 2000 * 50 + 87 * 10
        portfolioProcessor.onQuote(new Quote(instrument3.getInstId(), System.currentTimeMillis(), 87, 89, 1, 1));
        assertEquals(58870, portfolioProcessor.positionValue(), 0.0);

        portfolioProcessor.onBar(new Bar(instrument2.getInstId(), 60, System.currentTimeMillis(), 2000, 2100, 1900, 2050, 1, 1));

        //(-21000 * 2) + 2050 * 50 + 87 * 10
        assertEquals(61370, portfolioProcessor.positionValue(), 0.0);
    }

    @Test
    public void test_position_value_with_marketdate_update() {

        Order order = limitOrder(instrument1.getInstId(), 20000, 2, Side.Sell);
        ExecutionReport er = executionReport(order);
        order.add(er);
        portfolioProcessor.add(order);

        order = limitOrder(instrument2.getInstId(), 2000, 50, Side.Buy);
        er = executionReport(order);
        order.add(er);
        portfolioProcessor.add(order);

        order = limitOrder(instrument3.getInstId(), 85, 10, Side.Buy);
        er = executionReport(order);
        order.add(er);
        portfolioProcessor.add(order);


        //(-20000 * 2) + 2000 * 50 + 85 * 10
        assertEquals(60850, portfolioProcessor.positionValue(), 0.0);


        //(-20000 * 2) + 2000 * 50 + 88 * 10
        portfolioProcessor.onTrade(new Trade(instrument3.getInstId(), System.currentTimeMillis(), 88, 1));
        assertEquals(60880, portfolioProcessor.positionValue(), 0.0);

        //(-21000 * 2) + 2000 * 50 + 88 * 10
        portfolioProcessor.onQuote(new Quote(instrument1.getInstId(), System.currentTimeMillis(), 19000, 21000, 1, 1));
        assertEquals(58880, portfolioProcessor.positionValue(), 0.0);

        //(-21000 * 2) + 2000 * 50 + 87 * 10
        portfolioProcessor.onQuote(new Quote(instrument3.getInstId(), System.currentTimeMillis(), 87, 89, 1, 1));
        assertEquals(58870, portfolioProcessor.positionValue(), 0.0);

        portfolioProcessor.onBar(new Bar(instrument2.getInstId(), 60, System.currentTimeMillis(), 2000, 2100, 1900, 2050, 1, 1));

        //(-21000 * 2) + 2050 * 50 + 87 * 10
        assertEquals(61370, portfolioProcessor.positionValue(), 0.0);
    }

    @Test
    public void test_position_marign_value(){
        //TODO
    }

    @Test
    public void test_position_debt_value(){
        //TODO
    }

    @Test
    public void test_core_equity(){
        assertEquals(1_000_000, portfolioProcessor.accountValue(), 0.0);
        assertEquals(portfolioProcessor.accountValue(), portfolioProcessor.value(), 0.0);
        assertEquals(portfolioProcessor.coreEquity(), portfolioProcessor.accountValue(), 0.0);

        account.deposit(System.currentTimeMillis(), Currency.HKD, 1000, "");
        assertEquals(1_001_000, portfolioProcessor.accountValue(), 0.0);
        assertEquals(portfolioProcessor.accountValue(), portfolioProcessor.value(), 0.0);
        assertEquals(portfolioProcessor.coreEquity(), portfolioProcessor.accountValue(), 0.0);
    }

    @Test
    public void test_total_equity(){
        portfolioProcessor = spy(portfolioProcessor);

        when(portfolioProcessor.value()).thenReturn(999.0);
        when(portfolioProcessor.debtValue()).thenReturn(10000.0);
        assertEquals(-9001, portfolioProcessor.totalEquity(), 0.0);

        when(portfolioProcessor.value()).thenReturn(888.0);
        when(portfolioProcessor.debtValue()).thenReturn(60000.0);
        assertEquals(-59112, portfolioProcessor.totalEquity(), 0.0);
    }

    @Test
    public void test_leverage(){
        portfolioProcessor = spy(portfolioProcessor);

        when(portfolioProcessor.value()).thenReturn(10000.0);
        when(portfolioProcessor.marginValue()).thenReturn(2000.0);
        assertEquals(5, portfolioProcessor.leverage(), 0.0);

        when(portfolioProcessor.marginValue()).thenReturn(0.0);
        assertEquals(0, portfolioProcessor.leverage(), 0.0);
    }

    @Test
    public void test_debt_equity_ratio(){
        portfolioProcessor = spy(portfolioProcessor);

        when(portfolioProcessor.totalEquity()).thenReturn(10000.0);
        when(portfolioProcessor.debtValue()).thenReturn(2000.0);
        assertEquals(0.2, portfolioProcessor.debtEquityRatio(), 0.0);
    }

    @Test
    public void test_cash_flow(){
        Order order = limitOrder(instrument2.getInstId(), 2000, 50, Side.Buy);
        ExecutionReport er = executionReport(order);
        order.add(er);
        portfolioProcessor.add(order);
        assertEquals(-100000, portfolioProcessor.cashFlow(), 0.0);

        order = limitOrder(instrument1.getInstId(), 20000, 2, Side.Sell);
        er = executionReport(order);
        order.add(er);
        portfolioProcessor.add(order);

        assertEquals(-60000, portfolioProcessor.cashFlow(), 0.0);
    }


    @Test
    public void test_net_cash_flow(){
        Order order = limitOrder(instrument2.getInstId(), 2000, 50, Side.Buy);
        ExecutionReport er = executionReport(order);
        order.add(er);
        order.commissions.add(new Commission.AbsoluteCommission(1000).apply(order));
        portfolioProcessor.add(order);
        assertEquals(-101000, portfolioProcessor.cashFlow(), 0.0);


        order = limitOrder(instrument1.getInstId(), 20000, 2, Side.Sell);
        er = executionReport(order);
        order.add(er);
        order.commissions.add(new Commission.PerShareCommission(50).apply(order));
        portfolioProcessor.add(order);
        assertEquals(-61100, portfolioProcessor.cashFlow(), 0.0);
    }

    @Test
    public void test_partial_close_and_fully_close(){

        assertValue(account, portfolio, 1_000_000, 0.0, 1_000_000, 0.0, 0.0, 1_000_000);

        Order buyOrder = limitOrder(instrument1.getInstId(), 10000, 2, Side.Buy);
        ExecutionReport er1 = executionReport(buyOrder, 10000, 2);
        buyOrder.add(er1);
        portfolioProcessor.add(buyOrder);

        assertValue(account, portfolio,
                980_000,
                20_000,
                1_000_000,
                -20_000,
                -20_000,
                1_000_000);


        Order sellOrder1 = limitOrder(instrument1.getInstId(), 15000, 1, Side.Sell);
        ExecutionReport er2 = executionReport(sellOrder1, 15000, 1);
        sellOrder1.add(er2);
        portfolioProcessor.add(sellOrder1);


        assertValue(account, portfolio,
                995_000,
                15000 ,
                1_010_000,
                -5000,
                -5000,
                1_010_000);


        Order sellOrder2 = limitOrder(instrument1.getInstId(), 20000, 1, Side.Sell);
        ExecutionReport er3 = executionReport(sellOrder2, 20000, 1);
        sellOrder2.add(er3);
        portfolioProcessor.add(sellOrder2);


        assertValue(account, portfolio,
                1_015_000,
                0 ,
                1_015_000,
                0,
                0,
                1_015_000);
    }

    private void assertValue(Account account, Portfolio portfolio, double accountValue, double portfolioPositionValue,
                            double portfolioValue, double portfolioCashFlow,
                            double portfolioNetCashFlow, double portfolioTotalEquity){

        assertEquals("accountValue not match", accountValue, portfolioProcessor.accountValue(), 0.0);
        assertEquals("portfolioPositionValue not match", portfolioPositionValue, portfolioProcessor.positionValue(), 0.0);
        assertEquals("portfolioValue not match", portfolioValue, portfolioProcessor.value(), 0.0);
        assertEquals("portfolioCashFlow not match", portfolioCashFlow, portfolioProcessor.cashFlow(), 0.0);
        assertEquals("portfolioNetCashFlow not match", portfolioNetCashFlow, portfolioProcessor.netCashFlow(), 0.0);
        assertEquals("portfolioTotalEquity not match", portfolioTotalEquity, portfolioProcessor.totalEquity(), 0.0);

    }

//    private void logDebug(Account account, Portfolio portfolio, String stage){
//        LOG.info("stage = {}, account.getValue={}, portfolio.positionValue={}, portfolio.getValue={}, portfolio.cashFlow={}, portfolio.netCashFlow={}, portfolio.totalEquity={}",
//                stage, account.value(), portfolio.positionValue(), portfolio.value(), portfolio.cashFlow(), portfolio.netCashFlow(), portfolio.totalEquity());
//    }


    private Order limitOrder(int instId, double limitPrice, double qty, Side side){

        Order order = new Order();
        order.orderId = ordId ++ ;
        order.instId = instId;
        order.dateTime = System.currentTimeMillis();
        order.ordType = OrdType.Limit;
        order.limitPrice = limitPrice;
        order.ordQty = qty;
        order.side = side;
        order.tif = TimeInForce.Day;
        order.portfolioId = portfolioId;

        return order;
    }

    private ExecutionReport executionReport(Order order){
        return executionReport(order, order.limitPrice, order.ordQty);
    }

    private ExecutionReport executionReport(Order order, double fillPrice, double filledQty){

        ExecutionReport er = new ExecutionReport();
        er.orderId = order.orderId;
        er.instId = order.instId;
        er.execId = execId++;
        er.transactionTime = System.currentTimeMillis();
        er.ordType = order.ordType;
        er.limitPrice = order.limitPrice;
        er.ordQty = order.ordQty;

        er.side = order.side;
        er.tif = order.tif;

        er.filledQty = filledQty;
        er.avgPrice = fillPrice;

        er.lastQty = filledQty;
        er.lastPrice = fillPrice;
        er.ordStatus = OrdStatus.Filled;

        return er;

    }
}
