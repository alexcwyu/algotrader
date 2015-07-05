package com.unisoft.algotrader.persistence.cassandra.objectmapper;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.UDTMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.unisoft.algotrader.clock.Clock;
import com.unisoft.algotrader.core.*;
import com.unisoft.algotrader.event.SampleEventFactory;
import com.unisoft.algotrader.event.execution.ExecutionReport;
import com.unisoft.algotrader.event.execution.Order;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by alex on 7/2/15.
 */

public class CassandraDataStoreIntegrationTest {

    public static class CassandraDataStore {

        private Cluster cluster;
        private String keySpace;
        private Session session;
        private MappingManager mappingManager;

        public CassandraDataStore() {
            this(Cluster.builder().withProtocolVersion(ProtocolVersion.V3).addContactPoint("localhost").build(), "trading");
        }

        public CassandraDataStore(Cluster cluster, String keySpace) {
            this.cluster = cluster;
            this.keySpace = keySpace;
        }

        public void connect() {
            this.session = cluster.connect(keySpace);
            this.mappingManager = new MappingManager(session);
        }


        public void saveAccount(Account account) {
            Mapper<Account> mapper = mappingManager.mapper(com.unisoft.algotrader.core.Account.class);
            mapper.save(account);
        }

        public Account getAccount(String accountId) {
            Mapper<Account> mapper = mappingManager.mapper(Account.class);
            return mapper.get(accountId);
        }

        public void savePortfolio(Portfolio portfolio) {
            Mapper<Portfolio> mapper = mappingManager.mapper(Portfolio.class);
            mapper.save(portfolio);
        }

        public Portfolio getPortfolio(String portfolioId) {
            Mapper<Portfolio> mapper = mappingManager.mapper(Portfolio.class);
            return mapper.get(portfolioId);
        }

        public void saveExecutionReport(ExecutionReport er) {
            Mapper<ExecutionReport> mapper = mappingManager.mapper(ExecutionReport.class);
            mapper.save(er);
        }

        public ExecutionReport getExecutionReport(long execId) {
            Mapper<ExecutionReport> mapper = mappingManager.mapper(ExecutionReport.class);
            return mapper.get(execId);
        }

        public void saveOrder(Order order) {
            Mapper<Order> mapper = mappingManager.mapper(Order.class);
            mapper.save(order);
        }

        public Order getOrder(long orderId) {
            Mapper<Order> mapper = mappingManager.mapper(Order.class);
            return mapper.get(orderId);
        }
    }

    @Test
    public void testSaveLoad() throws Exception {

        CassandraDataStore store = new CassandraDataStore();
        store.connect();

        Account account = AccountManager.DEFAULT_ACCOUNT;

        Portfolio portfolio = SampleEventFactory.createPortfolio("TestPortfolio", account.getName());
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

        Account loadedAccount = store.getAccount(account.getName());
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
