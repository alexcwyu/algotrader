package com.unisoft.algotrader.persistence.cassandra;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.clock.Clock;
import com.unisoft.algotrader.model.event.SampleEventFactory;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.trading.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by alex on 7/2/15.
 */

public class CassandraTradingDataStoreIntegrationTest {

    @Test
    public void testSaveLoad() throws Exception {

        CassandraTradingDataStore store = new CassandraTradingDataStore();
        store.connect();

        Account account = AccountManager.DEFAULT_ACCOUNT;

        Portfolio portfolio = SampleEventFactory.createPortfolio("TestPortfolio", account.getAccountId());
        PortfolioManager.INSTANCE.add(portfolio);

        Clock.CLOCK.setDateTime(System.currentTimeMillis());
        Order order = SampleEventFactory.createOrder(SampleEventFactory.testInstrument2.getInstId(), Side.Buy, OrdType.Limit, 9000, 98, 0.0, TimeInForce.Day, "TESTIB", portfolio.getPortfolioId(), "TESTStrategy");
        ExecutionReport executionReport = SampleEventFactory.createExecutionReport(order);
        order.add(executionReport);
        portfolio.add(order);

        Clock.CLOCK.setDateTime(System.currentTimeMillis());
        Order order2 = SampleEventFactory.createOrder(SampleEventFactory.testInstrument2.getInstId(), Side.Sell, OrdType.Limit, 10000, 108, 0.0, TimeInForce.Day, "TESTIB", portfolio.getPortfolioId(), "TESTStrategy");
        ExecutionReport executionReport2 = SampleEventFactory.createExecutionReport(order2);
        order2.add(executionReport2);
        portfolio.add(order2);

        Clock.CLOCK.setDateTime(System.currentTimeMillis());
        Order order3 = SampleEventFactory.createOrder(SampleEventFactory.testInstrument2.getInstId(), Side.Buy, OrdType.Limit, 1000, 88, 0.0, TimeInForce.Day, "TESTIB", portfolio.getPortfolioId(), "TESTStrategy");
        ExecutionReport executionReport3 = SampleEventFactory.createExecutionReport(order3);
        order3.add(executionReport3);
        portfolio.add(order3);

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
        loadedPortfolio.setAccount(loadedAccount);

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
