package com.unisoft.algotrader.persistence.cassandra.objectmapper;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import com.unisoft.algotrader.clock.Clock;
import com.unisoft.algotrader.core.*;
import com.unisoft.algotrader.event.SampleEventFactory;
import com.unisoft.algotrader.event.execution.ExecutionReport;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.refdata.Exchange;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by alex on 7/2/15.
 */

public class CassandraDataStoreIntegrationTest {

    @Accessor
    public interface CurrencyAccessor {
        @Query("SELECT * FROM refdata.currency WHERE ccy_id = :ccy_id LIMIT 1")
        Currency getOne(@Param("ccy_id") String ccy_id);

        @Query("SELECT * FROM refdata.currency WHERE ccy_id = :ccy_id")
        Result<Currency> get(@Param("ccy_id") String ccy_id);

        @Query("SELECT * FROM refdata.currency")
        Result<Currency> getAll();

        @Query("SELECT * FROM refdata.currency")
        ListenableFuture<Result<Currency>> getAllAsync();
    }

    @Accessor
    public interface ExchangeAccessor {
        @Query("SELECT * FROM refdata.exchange WHERE exch_id = :exch_id LIMIT 1")
        Exchange getOne(@Param("exch_id") String exch_id);

        @Query("SELECT * FROM refdata.exchange WHERE exch_id = :exch_id")
        Result<Exchange> get(@Param("exch_id") String exch_id);

        @Query("SELECT * FROM refdata.exchange")
        Result<Exchange> getAll();

        @Query("SELECT * FROM refdata.exchange")
        ListenableFuture<Result<Exchange>> getAllAsync();
    }

    public static class RefDataStore{

        private Cluster cluster;
        private String keySpace;
        private Session session;
        private MappingManager mappingManager;
        private CurrencyAccessor currencyAccessor;
        private ExchangeAccessor exchangeAccessor;

        public RefDataStore() {
            this(Cluster.builder().withProtocolVersion(ProtocolVersion.V3).addContactPoint("localhost").build(), "refdata");
        }

        public RefDataStore(Cluster cluster, String keySpace) {
            this.cluster = cluster;
            this.keySpace = keySpace;
        }

        public void connect() {
            this.session = cluster.connect(keySpace);
            this.mappingManager = new MappingManager(session);
            this.currencyAccessor = mappingManager.createAccessor(CurrencyAccessor.class);
            this.exchangeAccessor = mappingManager.createAccessor(ExchangeAccessor.class);
        }

        public void saveCurrency(Currency currency) {
            Mapper<Currency> mapper = mappingManager.mapper(Currency.class);
            long time = System.currentTimeMillis();
            currency.setBusinesstime(time);
            currency.setSystemtime(time);
            mapper.save(currency);
        }

        public Currency getCurrency(String ccyId) {
            return currencyAccessor.getOne(ccyId);
        }

        public void saveExchange(Exchange exchange) {
            Mapper<Exchange> mapper = mappingManager.mapper(Exchange.class);
            long time = System.currentTimeMillis();
            exchange.setBusinesstime(time);
            exchange.setSystemtime(time);
            mapper.save(exchange);
        }

        public Exchange getExchange(String exchId) {
            return exchangeAccessor.getOne(exchId);
        }

        public void saveInstrument(Instrument instrument) {
            Mapper<Instrument> mapper = mappingManager.mapper(Instrument.class);
//            long time = System.currentTimeMillis();
//            instrument.setBusinessTime(time);
//            instrument.setSystemtime(time);
            mapper.save(instrument);
        }

        public Instrument getInstrument(String instId) {
            Mapper<Instrument> mapper = mappingManager.mapper(Instrument.class);
            return mapper.get(instId);
        }
    }

    public static class CassandraDataStore {

        private Cluster cluster;
        private Session session;
        private String keySpace;
        private MappingManager mappingManager;

        public CassandraDataStore() {
            this(Cluster.builder().withProtocolVersion(ProtocolVersion.V3).addContactPoint("localhost").build(), "trading");
        }

        public CassandraDataStore(Cluster cluster,String keySpace) {
            this.cluster = cluster;
            this.keySpace = keySpace;
        }

        public void connect() {
            this.session = cluster.connect(keySpace);
            this.mappingManager = new MappingManager(session);
        }


        public void saveAccount(Account account) {
            Mapper<Account> mapper = mappingManager.mapper(Account.class);
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


    @Test
    public void testSaveLoadRefData() throws Exception {

        RefDataStore store = new RefDataStore();
        store.connect();

        Currency currency = new Currency("USD", "US Dollar");
        store.saveCurrency(currency);
        Currency currency2 = store.getCurrency(currency.getCcyId());

        assertEquals(currency, currency2);

        Exchange exchange = new Exchange("SEHK", "Hong Kong Stock Exchange");
        store.saveExchange(exchange);
        Exchange exchange1 = store.getExchange(exchange.getExchId());

        assertEquals(exchange, exchange1);
    }
}
