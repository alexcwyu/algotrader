package com.unisoft.algotrader.persistence.cassandra;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.event.SampleEventFactory;
import com.unisoft.algotrader.model.clock.Clock;
import com.unisoft.algotrader.model.clock.SimulationClock;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.trading.*;
import com.unisoft.algotrader.persistence.SampleInMemoryRefDataStore;
import com.unisoft.algotrader.persistence.TradingDataStore;
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
        Account account1 = store.getAccount(account.accountId());
        assertEquals(account, account1);

        assertTrue(store.getAllAccounts().contains(account));

    }


    @Test
    public void testSaveLoadPortfolio(){
        Portfolio portfolio = new Portfolio(1, "TA1");
        store.savePortfolio(portfolio);
        Portfolio portfolio1 = store.getPortfolio(portfolio.portfolioId());
        assertEquals(portfolio, portfolio1);

        assertTrue(store.getAllPortfolios().contains(portfolio));

    }

    @Test
    public void testSaveLoadOrder(){
        Order order = SampleEventFactory.createOrder(1001, Side.Buy, OrdType.Limit, 9000, 98, 0.0, TimeInForce.Day, 1, 1, 1);

        store.saveOrder(order);
        Order order1 = store.getOrder(order.clOrderId());
        assertEquals(order, order1);

        assertTrue(store.getAllOrders().contains(order));
        assertTrue(store.getOrdersByInstId(order.instId()).contains(order));
        assertTrue(store.getOrdersByPortfolioId(order.portfolioId()).contains(order));
        assertTrue(store.getOrdersByStrategyId(order.strategyId()).contains(order));
    }

    @Test
    public void testSaveLoadExecutionReport(){
        ExecutionReport executionReport = SampleEventFactory.createExecutionReport(1102, 1001, Side.Buy, OrdType.Limit, 9000, 98, 0.0, TimeInForce.Day, OrdStatus.New, 0, 0, 0, 0);

        store.saveExecutionReport(executionReport);
        ExecutionReport executionReport1 = store.getExecutionReport(executionReport.execId());
        assertEquals(executionReport, executionReport1);

        assertTrue(store.getAllExecutionReports().contains(executionReport));
        assertTrue(store.getExecutionReportsByInstId(executionReport.instId()).contains(executionReport));
        assertTrue(store.getExecutionReportsByOrderId(executionReport.clOrderId()).contains(executionReport));
    }

    @Test
    public void testSaveLoad() throws Exception {
        Account account = TradingDataStore.DEFAULT_ACCOUNT;

        Portfolio portfolio = SampleEventFactory.createPortfolio(1, account.accountId());
        Clock clock = new SimulationClock();
        PortfolioProcessor portfolioProcessor = new PortfolioProcessor(portfolio, account, new SampleInMemoryRefDataStore(), clock);

        clock.setDateTime(System.currentTimeMillis());
        Order order = SampleEventFactory.createOrder(SampleEventFactory.TEST_USD_INSTRUMENT.getInstId(), Side.Buy, OrdType.Limit, 9000, 98, 0.0, TimeInForce.Day, 1, portfolio.portfolioId(), 1);
        ExecutionReport executionReport = SampleEventFactory.createExecutionReport(order);
        order.add(executionReport);
        portfolioProcessor.add(order);

        clock.setDateTime(System.currentTimeMillis());
        Order order2 = SampleEventFactory.createOrder(SampleEventFactory.TEST_USD_INSTRUMENT.getInstId(), Side.Sell, OrdType.Limit, 10000, 108, 0.0, TimeInForce.Day, 1, portfolio.portfolioId(), 1);
        ExecutionReport executionReport2 = SampleEventFactory.createExecutionReport(order2);
        order2.add(executionReport2);
        portfolioProcessor.add(order2);

        clock.setDateTime(System.currentTimeMillis());
        Order order3 = SampleEventFactory.createOrder(SampleEventFactory.TEST_USD_INSTRUMENT.getInstId(), Side.Buy, OrdType.Limit, 1000, 88, 0.0, TimeInForce.Day, 1, portfolio.portfolioId(), 1);
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


        Order loadedOrder = store.getOrder(order.clOrderId());
        assertNotNull(loadedOrder);
        Order loadedOrder2 = store.getOrder(order2.clOrderId());
        assertNotNull(loadedOrder2);
        Order loadedOrder3 = store.getOrder(order3.clOrderId());
        assertNotNull(loadedOrder2);

        ExecutionReport loadedER = store.getExecutionReport(executionReport.execId());
        assertNotNull(loadedER);
        ExecutionReport loadedER2 = store.getExecutionReport(executionReport2.execId());
        assertNotNull(loadedER2);
        ExecutionReport loadedER3 = store.getExecutionReport(executionReport3.execId());
        assertNotNull(loadedER3);

        loadedOrder.executionReports(Lists.newArrayList(loadedER));
        loadedOrder2.executionReports(Lists.newArrayList(loadedER2));
        loadedOrder3.executionReports(Lists.newArrayList(loadedER3));

        Portfolio loadedPortfolio = store.getPortfolio(portfolio.portfolioId());
        assertNotNull(loadedPortfolio);
        loadedPortfolio.orderList(Lists.newArrayList(loadedOrder, loadedOrder2, loadedOrder3));

        Account loadedAccount = store.getAccount(account.accountId());
        assertNotNull(loadedAccount);
        loadedPortfolio.accountId(loadedAccount.accountId());

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
