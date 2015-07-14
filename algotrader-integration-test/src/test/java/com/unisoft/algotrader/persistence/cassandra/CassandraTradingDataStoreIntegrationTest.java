package com.unisoft.algotrader.persistence.cassandra;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.event.SampleEventFactory;
import com.unisoft.algotrader.model.clock.Clock;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.trading.*;
import com.unisoft.algotrader.persistence.SampleInMemoryRefDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.trading.PortfolioManager;
import com.unisoft.algotrader.trading.PortfolioProcessor;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by alex on 7/2/15.
 */

public class CassandraTradingDataStoreIntegrationTest {


    private static CassandraTradingDataStore store;

    @BeforeClass
    public static void init(){
        store = new CassandraTradingDataStore();
        store.connect();
    }

    @Test
    public void testSaveLoadAccount(){
        Account account = new Account("TA1", "Testing Account 1", Currency.HKD, 1000000);
        store.saveAccount(account);
        Account account1 = store.getAccount(account.getAccountId());
        assertEquals(account, account1);

        assertTrue(store.getAllAccounts().contains(account));

    }


    @Test
    public void testSaveLoadPortfolio(){
        Portfolio portfolio = new Portfolio("TP1", "TA1");
        store.savePortfolio(portfolio);
        Portfolio portfolio1 = store.getPortfolio(portfolio.getPortfolioId());
        assertEquals(portfolio, portfolio1);

        assertTrue(store.getAllPortfolios().contains(portfolio));

    }

    @Test
    public void testSaveLoadOrder(){
        Order order = SampleEventFactory.createOrder(1001, Side.Buy, OrdType.Limit, 9000, 98, 0.0, TimeInForce.Day, "Provider1", "Portfolio1", "Strategy1");

        store.saveOrder(order);
        Order order1 = store.getOrder(order.getOrderId());
        assertEquals(order, order1);

        assertTrue(store.getAllOrders().contains(order));
        assertTrue(store.getOrdersByInstId(order.getInstId()).contains(order));
        assertTrue(store.getOrdersByPortfolioId(order.getPortfolioId()).contains(order));
        assertTrue(store.getOrdersByStrategyId(order.getStrategyId()).contains(order));
    }

    @Test
    public void testSaveLoadExecutionReport(){
        ExecutionReport executionReport = SampleEventFactory.createExecutionReport(1102, 1001, Side.Buy, OrdType.Limit, 9000, 98, 0.0, TimeInForce.Day, OrdStatus.New, 0, 0, 0, 0);

        store.saveExecutionReport(executionReport);
        ExecutionReport executionReport1 = store.getExecutionReport(executionReport.getExecId());
        assertEquals(executionReport, executionReport1);

        assertTrue(store.getAllExecutionReports().contains(executionReport));
        assertTrue(store.getExecutionReportsByInstId(executionReport.getInstId()).contains(executionReport));
        assertTrue(store.getExecutionReportsByOrderId(executionReport.getOrderId()).contains(executionReport));
    }

    @Test
    public void testSaveLoad() throws Exception {
        Account account = TradingDataStore.DEFAULT_ACCOUNT;

        Portfolio portfolio = SampleEventFactory.createPortfolio("TestPortfolio", account.getAccountId());
        PortfolioProcessor portfolioProcessor = new PortfolioProcessor(portfolio, account, new SampleInMemoryRefDataStore(), Clock.CLOCK);
        PortfolioManager.INSTANCE.add(portfolio);

        Clock.CLOCK.setDateTime(System.currentTimeMillis());
        Order order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument2.getInstId(), Side.Buy, OrdType.Limit, 9000, 98, 0.0, TimeInForce.Day, "TESTIB", portfolio.getPortfolioId(), "TESTStrategy");
        ExecutionReport executionReport = SampleEventFactory.createExecutionReport(order);
        order.add(executionReport);
        portfolioProcessor.add(order);

        Clock.CLOCK.setDateTime(System.currentTimeMillis());
        Order order2 = SampleEventFactory.createOrder(SampleEventFactory.testInstrument2.getInstId(), Side.Sell, OrdType.Limit, 10000, 108, 0.0, TimeInForce.Day, "TESTIB", portfolio.getPortfolioId(), "TESTStrategy");
        ExecutionReport executionReport2 = SampleEventFactory.createExecutionReport(order2);
        order2.add(executionReport2);
        portfolioProcessor.add(order2);

        Clock.CLOCK.setDateTime(System.currentTimeMillis());
        Order order3 = SampleEventFactory.createOrder(SampleEventFactory.testInstrument2.getInstId(), Side.Buy, OrdType.Limit, 1000, 88, 0.0, TimeInForce.Day, "TESTIB", portfolio.getPortfolioId(), "TESTStrategy");
        ExecutionReport executionReport3 = SampleEventFactory.createExecutionReport(order3);
        order3.add(executionReport3);
        portfolioProcessor.add(order3);

        store.saveOrder(order);
        store.saveOrder(order2);
        store.saveOrder(order3);

        store.saveExecutionReport(executionReport);
        store.saveExecutionReport(executionReport2);
        store.saveExecutionReport(executionReport3);

        store.savePortfolio(portfolio);
        store.saveAccount(account);


        Order loadedOrder = store.getOrder(order.getOrderId());
        assertNotNull(loadedOrder);
        Order loadedOrder2 = store.getOrder(order2.getOrderId());
        assertNotNull(loadedOrder2);
        Order loadedOrder3 = store.getOrder(order3.getOrderId());
        assertNotNull(loadedOrder2);

        ExecutionReport loadedER = store.getExecutionReport(executionReport.getExecId());
        assertNotNull(loadedER);
        ExecutionReport loadedER2 = store.getExecutionReport(executionReport2.getExecId());
        assertNotNull(loadedER2);
        ExecutionReport loadedER3 = store.getExecutionReport(executionReport3.getExecId());
        assertNotNull(loadedER3);

        loadedOrder.setExecutionReports(Lists.newArrayList(loadedER));
        loadedOrder2.setExecutionReports(Lists.newArrayList(loadedER2));
        loadedOrder3.setExecutionReports(Lists.newArrayList(loadedER3));

        Portfolio loadedPortfolio = store.getPortfolio(portfolio.getPortfolioId());
        assertNotNull(loadedPortfolio);
        loadedPortfolio.setOrderList(Lists.newArrayList(loadedOrder, loadedOrder2, loadedOrder3));

        Account loadedAccount = store.getAccount(account.getAccountId());
        assertNotNull(loadedAccount);
        loadedPortfolio.setAccountName(loadedAccount.getName());

        assertEquals(order, loadedOrder);
        assertEquals(order2, loadedOrder2);
        assertEquals(order3, loadedOrder3);

        assertEquals(executionReport, loadedER);
        assertEquals(executionReport2, loadedER2);
        assertEquals(executionReport3, loadedER3);

        assertEquals(portfolio, loadedPortfolio);
        assertEquals(account, loadedAccount);
    }



}
