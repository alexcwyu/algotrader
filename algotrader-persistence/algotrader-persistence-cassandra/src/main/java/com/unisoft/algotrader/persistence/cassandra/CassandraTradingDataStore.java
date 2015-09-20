package com.unisoft.algotrader.persistence.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.trading.Account;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.persistence.TradingDataStore;
import com.unisoft.algotrader.persistence.cassandra.accessor.AccountAccessor;
import com.unisoft.algotrader.persistence.cassandra.accessor.ExecutionReportAccessor;
import com.unisoft.algotrader.persistence.cassandra.accessor.OrderAccessor;
import com.unisoft.algotrader.persistence.cassandra.accessor.PortfolioAccessor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by alex on 7/7/15.
 */
@Singleton
public class CassandraTradingDataStore implements TradingDataStore {
    private Cluster cluster;
    private Session session;
    private String keySpace;
    private MappingManager mappingManager;
    private AccountAccessor accountAccessor;
    private PortfolioAccessor portfolioAccessor;
    private ExecutionReportAccessor executionReportAccessor;
    private OrderAccessor orderAccessor;
    private CassandraIdSupplier idSupplier;

    @Inject
    public CassandraTradingDataStore(CassandraTradingDataStoreConfig config) {
        this(Cluster.builder().withProtocolVersion(ProtocolVersion.V3).addContactPoint(config.host).build(), config.keyspace);
    }

    public CassandraTradingDataStore() {
        this(Cluster.builder().withProtocolVersion(ProtocolVersion.V3).addContactPoint("localhost").build(), "trading");
    }

    public CassandraTradingDataStore(Cluster cluster, String keySpace) {
        this.cluster = cluster;
        this.keySpace = keySpace;
    }

    public void connect() {
        this.session = cluster.connect(keySpace);
        this.mappingManager = new MappingManager(session);
        this.accountAccessor = mappingManager.createAccessor(AccountAccessor.class);
        this.portfolioAccessor = mappingManager.createAccessor(PortfolioAccessor.class);
        this.executionReportAccessor = mappingManager.createAccessor(ExecutionReportAccessor.class);
        this.orderAccessor = mappingManager.createAccessor(OrderAccessor.class);
        this.idSupplier = new CassandraIdSupplier(session, keySpace);
    }

    @Override
    public void saveAccount(Account account) {
        Mapper<Account> mapper = mappingManager.mapper(Account.class);
        mapper.save(account);
    }

    @Override
    public Account getAccount(String accountId) {
        Mapper<Account> mapper = mappingManager.mapper(Account.class);
        return mapper.get(accountId);
    }

    @Override
    public void savePortfolio(Portfolio portfolio) {
        Mapper<Portfolio> mapper = mappingManager.mapper(Portfolio.class);
        mapper.save(portfolio);
    }

    @Override
    public Portfolio getPortfolio(int portfolioId) {
        Mapper<Portfolio> mapper = mappingManager.mapper(Portfolio.class);
        return mapper.get(portfolioId);
    }

    @Override
    public void saveExecutionReport(ExecutionReport er) {
        Mapper<ExecutionReport> mapper = mappingManager.mapper(ExecutionReport.class);
        mapper.save(er);
    }

    @Override
    public ExecutionReport getExecutionReport(long execId) {
        Mapper<ExecutionReport> mapper = mappingManager.mapper(ExecutionReport.class);
        return mapper.get(execId);
    }

    @Override
    public void saveOrder(Order order) {
        Mapper<Order> mapper = mappingManager.mapper(Order.class);
        mapper.save(order);
    }

    @Override
    public Order getOrder(long clOrderId) {
        Mapper<Order> mapper = mappingManager.mapper(Order.class);
        return mapper.get(clOrderId);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountAccessor.getAll().all();
    }

    @Override
    public List<Portfolio> getAllPortfolios() {
        return portfolioAccessor.getAll().all();
    }

    @Override
    public List<ExecutionReport> getAllExecutionReports() {
        return executionReportAccessor.getAll().all();
    }

    @Override
    public List<ExecutionReport> getExecutionReportsByInstId(long instId) {
        return executionReportAccessor.getByInstId(instId).all();
    }

    @Override
    public List<ExecutionReport> getExecutionReportsByOrderId(long clOrderId) {
        return executionReportAccessor.getByOrderId(clOrderId).all();
    }

    @Override
    public List<Order> getAllOrders() {
        return orderAccessor.getAll().all();
    }

    @Override
    public List<Order> getOrdersByInstId(long instId) {
        return orderAccessor.getByInstId(instId).all();
    }

    @Override
    public List<Order> getOrdersByPortfolioId(int portfolioId) {
        return orderAccessor.getByPortfolioId(portfolioId).all();
    }

    @Override
    public List<Order> getOrdersByStrategyId(int strategyId) {
        return orderAccessor.getByStrategyId(strategyId).all();
    }

    @Override
    public long nextId() {
        return idSupplier.next();
    }
}
