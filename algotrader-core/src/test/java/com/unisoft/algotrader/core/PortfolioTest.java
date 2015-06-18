package com.unisoft.algotrader.core;

import com.unisoft.algotrader.core.id.InstId;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.event.execution.ExecutionReport;
import com.unisoft.algotrader.event.execution.Order;
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
public class PortfolioTest {

    private static final Logger LOG = LogManager.getLogger(PortfolioTest.class);

    public static long ordId = 0;
    public static long execId = 100;
    public static String portfolioId = "TestPortfolio";
    private static Instrument instrument1 = new Instrument(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(),"HKD");
    private static Instrument instrument2 = new Instrument(InstId.Builder.as().symbol("0005.HK").exchId("HKEX").build(),"HKD");
    private static Instrument instrument3= new Instrument(InstId.Builder.as().symbol("0959.HK").exchId("HKEX").build(),"HKD");

    private Account account;
    private Portfolio portfolio;

    @BeforeClass
    public static void init() {
        InstrumentManager.INSTANCE.add(instrument1);
        InstrumentManager.INSTANCE.add(instrument2);
        InstrumentManager.INSTANCE.add(instrument3);
    }

    @Before
    public void setup() {
        account = new Account("Test Account", "Test",  Currency.HKD, 1_000_000);
        portfolio = new Portfolio(portfolioId, account);
    }

    @Test
    public void test_value(){
        portfolio = spy(portfolio);

        when(portfolio.accountValue()).thenReturn(999.0);
        when(portfolio.positionValue()).thenReturn(10000.0);
        assertEquals(10999.0, portfolio.value(), 0.0);

        when(portfolio.accountValue()).thenReturn(888.0);
        when(portfolio.positionValue()).thenReturn(60000.0);
        assertEquals(60888, portfolio.value(), 0.0);
    }


    @Test
    public void test_account_value(){
        assertEquals(1_000_000, account.value(), 0.0);
        assertEquals(portfolio.value(), account.value(), 0.0);

        account.deposit(System.currentTimeMillis(), Currency.HKD, 1000, "");
        assertEquals(1_001_000, account.value(), 0.0);
        assertEquals(portfolio.accountValue(), account.value(), 0.0);

    }

    @Test
    public void test_position_value(){

        assertEquals(0.0, portfolio.positionValue(), 0.0);

        Order order = limitOrder(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), 10000, 2, Side.Buy);
        ExecutionReport er = executionReport(order);
        order.add(er);
        portfolio.add(order);

        //10000 * 2
        assertEquals(20000, portfolio.positionValue(), 0.0);

        order = limitOrder(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), 10000, 1, Side.Buy);
        er = executionReport(order);
        order.add(er);
        portfolio.add(order);

        //10000 * 3
        assertEquals(30000, portfolio.positionValue(), 0.0);

        order = limitOrder(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), 18000, 1, Side.Buy);
        er = executionReport(order);
        order.add(er);
        portfolio.add(order);

        //18000 * 4
        assertEquals(72000, portfolio.positionValue(), 0.0);

        order = limitOrder(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), 20000, 6, Side.Sell);
        er = executionReport(order);
        order.add(er);
        portfolio.add(order);

        //-20000 * 2
        assertEquals(-40000, portfolio.positionValue(), 0.0);

        order = limitOrder(InstId.Builder.as().symbol("0005.HK").exchId("HKEX").build(), 2000, 50, Side.Buy);
        er = executionReport(order);
        order.add(er);
        portfolio.add(order);
        //(-20000 * 2) + 2000 * 50
        assertEquals(60000, portfolio.positionValue(), 0.0);

        order = limitOrder(InstId.Builder.as().symbol("0959.HK").exchId("HKEX").build(), 85, 10, Side.Buy);
        er = executionReport(order);
        order.add(er);
        portfolio.add(order);

        //(-20000 * 2) + 2000 * 50 + 85 * 10
        assertEquals(60850, portfolio.positionValue(), 0.0);


        //(-20000 * 2) + 2000 * 50 + 88 * 10
        portfolio.onTrade(new Trade(InstId.Builder.as().symbol("0959.HK").exchId("HKEX").build(), System.currentTimeMillis(), 88, 1));
        assertEquals(60880, portfolio.positionValue(), 0.0);

        //(-21000 * 2) + 2000 * 50 + 88 * 10
        portfolio.onQuote(new Quote(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), System.currentTimeMillis(), 19000, 21000, 1, 1));
        assertEquals(58880, portfolio.positionValue(), 0.0);

        //(-21000 * 2) + 2000 * 50 + 87 * 10
        portfolio.onQuote(new Quote(InstId.Builder.as().symbol("0959.HK").exchId("HKEX").build(), System.currentTimeMillis(), 87, 89, 1, 1));
        assertEquals(58870, portfolio.positionValue(), 0.0);

        portfolio.onBar(new Bar(InstId.Builder.as().symbol("0005.HK").exchId("HKEX").build(), System.currentTimeMillis(), 60, 2100, 1900, 2000, 2050, 1, 1));

        //(-21000 * 2) + 2050 * 50 + 87 * 10
        assertEquals(61370, portfolio.positionValue(), 0.0);
    }

    @Test
    public void test_position_value_with_marketdate_update() {

        Order order = limitOrder(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), 20000, 2, Side.Sell);
        ExecutionReport er = executionReport(order);
        order.add(er);
        portfolio.add(order);

        order = limitOrder(InstId.Builder.as().symbol("0005.HK").exchId("HKEX").build(), 2000, 50, Side.Buy);
        er = executionReport(order);
        order.add(er);
        portfolio.add(order);

        order = limitOrder(InstId.Builder.as().symbol("0959.HK").exchId("HKEX").build(), 85, 10, Side.Buy);
        er = executionReport(order);
        order.add(er);
        portfolio.add(order);


        //(-20000 * 2) + 2000 * 50 + 85 * 10
        assertEquals(60850, portfolio.positionValue(), 0.0);


        //(-20000 * 2) + 2000 * 50 + 88 * 10
        portfolio.onTrade(new Trade(InstId.Builder.as().symbol("0959.HK").exchId("HKEX").build(), System.currentTimeMillis(), 88, 1));
        assertEquals(60880, portfolio.positionValue(), 0.0);

        //(-21000 * 2) + 2000 * 50 + 88 * 10
        portfolio.onQuote(new Quote(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), System.currentTimeMillis(), 19000, 21000, 1, 1));
        assertEquals(58880, portfolio.positionValue(), 0.0);

        //(-21000 * 2) + 2000 * 50 + 87 * 10
        portfolio.onQuote(new Quote(InstId.Builder.as().symbol("0959.HK").exchId("HKEX").build(), System.currentTimeMillis(), 87, 89, 1, 1));
        assertEquals(58870, portfolio.positionValue(), 0.0);

        portfolio.onBar(new Bar(InstId.Builder.as().symbol("0005.HK").exchId("HKEX").build(), System.currentTimeMillis(), 60, 2100, 1900, 2000, 2050, 1, 1));

        //(-21000 * 2) + 2050 * 50 + 87 * 10
        assertEquals(61370, portfolio.positionValue(), 0.0);
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
        assertEquals(1_000_000, account.value(), 0.0);
        assertEquals(portfolio.accountValue(), account.value(), 0.0);
        assertEquals(portfolio.coreEquity(), account.value(), 0.0);

        account.deposit(System.currentTimeMillis(), Currency.HKD, 1000, "");
        assertEquals(1_001_000, account.value(), 0.0);
        assertEquals(portfolio.accountValue(), account.value(), 0.0);
        assertEquals(portfolio.coreEquity(), account.value(), 0.0);
    }

    @Test
    public void test_total_equity(){
        portfolio = spy(portfolio);

        when(portfolio.value()).thenReturn(999.0);
        when(portfolio.debtValue()).thenReturn(10000.0);
        assertEquals(-9001, portfolio.totalEquity(), 0.0);

        when(portfolio.value()).thenReturn(888.0);
        when(portfolio.debtValue()).thenReturn(60000.0);
        assertEquals(-59112, portfolio.totalEquity(), 0.0);
    }

    @Test
    public void test_leverage(){
        portfolio = spy(portfolio);

        when(portfolio.value()).thenReturn(10000.0);
        when(portfolio.marginValue()).thenReturn(2000.0);
        assertEquals(5, portfolio.leverage(), 0.0);

        when(portfolio.marginValue()).thenReturn(0.0);
        assertEquals(0, portfolio.leverage(), 0.0);
    }

    @Test
    public void test_debt_equity_ratio(){
        portfolio = spy(portfolio);

        when(portfolio.totalEquity()).thenReturn(10000.0);
        when(portfolio.debtValue()).thenReturn(2000.0);
        assertEquals(0.2, portfolio.debtEquityRatio(), 0.0);
    }

    @Test
    public void test_cash_flow(){
        Order order = limitOrder(InstId.Builder.as().symbol("0005.HK").exchId("HKEX").build(), 2000, 50, Side.Buy);
        ExecutionReport er = executionReport(order);
        order.add(er);
        portfolio.add(order);
        assertEquals(-100000, portfolio.cashFlow(), 0.0);

        order = limitOrder(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), 20000, 2, Side.Sell);
        er = executionReport(order);
        order.add(er);
        portfolio.add(order);

        assertEquals(-60000, portfolio.cashFlow(), 0.0);
    }


    @Test
    public void test_net_cash_flow(){
        Order order = limitOrder(InstId.Builder.as().symbol("0005.HK").exchId("HKEX").build(), 2000, 50, Side.Buy);
        ExecutionReport er = executionReport(order);
        order.add(er);
        order.commissions.add(new Commission.AbsoluteCommission(1000));
        portfolio.add(order);
        assertEquals(-101000, portfolio.cashFlow(), 0.0);


        order = limitOrder(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), 20000, 2, Side.Sell);
        er = executionReport(order);
        order.add(er);
        order.commissions.add(new Commission.PerShareCommission(50));
        portfolio.add(order);
        assertEquals(-61100, portfolio.cashFlow(), 0.0);
    }

    @Test
    public void test_partial_close_and_fully_close(){

        assertValue(account, portfolio, 1_000_000, 0.0, 1_000_000, 0.0, 0.0, 1_000_000);

        Order buyOrder = limitOrder(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), 10000, 2, Side.Buy);
        ExecutionReport er1 = executionReport(buyOrder, 10000, 2);
        buyOrder.add(er1);
        portfolio.add(buyOrder);

        assertValue(account, portfolio,
                980_000,
                20_000,
                1_000_000,
                -20_000,
                -20_000,
                1_000_000);


        Order sellOrder1 = limitOrder(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), 15000, 1, Side.Sell);
        ExecutionReport er2 = executionReport(sellOrder1, 15000, 1);
        sellOrder1.add(er2);
        portfolio.add(sellOrder1);


        assertValue(account, portfolio,
                995_000,
                15000 ,
                1_010_000,
                -5000,
                -5000,
                1_010_000);


        Order sellOrder2 = limitOrder(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), 20000, 1, Side.Sell);
        ExecutionReport er3 = executionReport(sellOrder2, 20000, 1);
        sellOrder2.add(er3);
        portfolio.add(sellOrder2);


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

        assertEquals("accountValue not match", accountValue, account.value(), 0.0);
        assertEquals("accountValue not match", portfolio.accountValue(), account.value(), 0.0);
        assertEquals("portfolioPositionValue not match", portfolioPositionValue, portfolio.positionValue(), 0.0);
        assertEquals("portfolioValue not match", portfolioValue, portfolio.value(), 0.0);
        assertEquals("portfolioCashFlow not match", portfolioCashFlow, portfolio.cashFlow(), 0.0);
        assertEquals("portfolioNetCashFlow not match", portfolioNetCashFlow, portfolio.netCashFlow(), 0.0);
        assertEquals("portfolioTotalEquity not match", portfolioTotalEquity, portfolio.totalEquity(), 0.0);

    }

    private void logDebug(Account account, Portfolio portfolio, String stage){
        LOG.info("stage = {}, account.value={}, portfolio.positionValue={}, portfolio.value={}, portfolio.cashFlow={}, portfolio.netCashFlow={}, portfolio.totalEquity={}",
                stage, account.value(), portfolio.positionValue(), portfolio.value(), portfolio.cashFlow(), portfolio.netCashFlow(), portfolio.totalEquity());
    }


    private Order limitOrder(InstId instId, double limitPx, double qty, Side side){

        Order order = new Order();
        order.orderId = ordId ++ ;
        order.instId = instId;
        order.dateTime = System.currentTimeMillis();
        order.ordType = OrdType.Limit;
        order.limitPrice = limitPx;
        order.ordQty = qty;
        order.side = side;
        order.tif = TimeInForce.Day;
        order.portfolioId = portfolioId;

        return order;
    }

    private ExecutionReport executionReport(Order order){
        return executionReport(order, order.limitPrice, order.ordQty);
    }

    private ExecutionReport executionReport(Order order, double fillPx, double filledQty){

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
        er.avgPx = fillPx;

        er.lastQty = filledQty;
        er.lastPrice = fillPx;
        er.ordStatus = OrdStatus.Filled;

        return er;

    }
}
