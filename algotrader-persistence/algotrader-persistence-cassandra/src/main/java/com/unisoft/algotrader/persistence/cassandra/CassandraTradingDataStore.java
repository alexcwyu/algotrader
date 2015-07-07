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

/**
 * Created by alex on 7/7/15.
 */
public class CassandraTradingDataStore implements TradingDataStore{
    private Cluster cluster;
    private Session session;
    private String keySpace;
    private MappingManager mappingManager;

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
