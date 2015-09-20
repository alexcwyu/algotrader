package com.unisoft.algotrader.persistence;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.trading.Account;
import com.unisoft.algotrader.model.trading.Portfolio;

import javax.inject.Singleton;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by alex on 7/9/15.
 */
@Singleton
public class InMemoryTradingDataStore implements TradingDataStore {

    private final TradingDataStore delegateDataStore;

    private final InMemoryIdSupplier idSupplier = new InMemoryIdSupplier();

    private LoadingCache<String, Account> accountCache = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<String, Account>() {
                        public Account load(String accountId) {
                            return delegateDataStore.getAccount(accountId);
                        }
                    });

    private LoadingCache<Integer, Portfolio> portfolioCache = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<Integer, Portfolio>() {
                        public Portfolio load(Integer portfolioId) {
                            return delegateDataStore.getPortfolio(portfolioId);
                        }
                    });


    private LoadingCache<Long, ExecutionReport> executionReportCache = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<Long, ExecutionReport>() {
                        public ExecutionReport load(Long execId) {
                            return delegateDataStore.getExecutionReport(execId);
                        }
                    });


    private LoadingCache<Long, Order> orderCache = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<Long, Order>() {
                        public Order load(Long clOrderId) {
                            return delegateDataStore.getOrder(clOrderId);
                        }
                    });


    public InMemoryTradingDataStore(TradingDataStore delegateDataStore){
        this.delegateDataStore = delegateDataStore;
    }


    public InMemoryTradingDataStore(){
        this.delegateDataStore = null;
    }

    @Override
    public void connect(){
        if(delegateDataStore != null) {
            delegateDataStore.connect();
            delegateDataStore.getAllAccounts().forEach(a -> accountCache.put(a.accountId(), a));
            delegateDataStore.getAllPortfolios().forEach(p -> portfolioCache.put(p.portfolioId(), p));
            delegateDataStore.getAllOrders().forEach(o -> orderCache.put(o.clOrderId(), o));
            delegateDataStore.getAllExecutionReports().forEach(er -> executionReportCache.put(er.execId(), er));
        }
    }

    @Override
    public void saveAccount(Account account) {
        if(delegateDataStore != null) {
            delegateDataStore.saveAccount(account);
        }
        accountCache.put(account.accountId(), account);
    }

    @Override
    public Account getAccount(String accountId) {
        return accountCache.getUnchecked(accountId);
    }

    @Override
    public List<Account> getAllAccounts() {
        return Lists.newArrayList(accountCache.asMap().values());
    }

    @Override
    public void savePortfolio(Portfolio portfolio) {
        if(delegateDataStore != null) {
            delegateDataStore.savePortfolio(portfolio);
        }
        portfolioCache.put(portfolio.portfolioId(), portfolio);
    }

    @Override
    public Portfolio getPortfolio(int portfolioId) {
        return portfolioCache.getUnchecked(portfolioId);
    }

    @Override
    public List<Portfolio> getAllPortfolios() {
        return Lists.newArrayList(portfolioCache.asMap().values());
    }

    @Override
    public void saveExecutionReport(ExecutionReport er) {
        if(delegateDataStore != null) {
            delegateDataStore.saveExecutionReport(er);
        }
        executionReportCache.put(er.execId(), er);
    }

    @Override
    public ExecutionReport getExecutionReport(long execId) {
        return executionReportCache.getUnchecked(execId);
    }

    @Override
    public List<ExecutionReport> getAllExecutionReports() {
        return Lists.newArrayList(executionReportCache.asMap().values());
    }

    @Override
    public List<ExecutionReport> getExecutionReportsByInstId(long instId) {
        return executionReportCache.asMap().values().stream().filter(er -> er.instId() == instId).collect(toList());
    }

    @Override
    public List<ExecutionReport> getExecutionReportsByOrderId(long clOrderId) {
        return executionReportCache.asMap().values().stream().filter(er -> er.clOrderId() == clOrderId).collect(toList());
    }

    @Override
    public void saveOrder(Order order) {
        if(delegateDataStore != null) {
            delegateDataStore.saveOrder(order);
        }
        orderCache.put(order.clOrderId(), order);
    }

    @Override
    public Order getOrder(long clOrderId) {
        return orderCache.getUnchecked(clOrderId);
    }

    @Override
    public List<Order> getAllOrders() {
        return Lists.newArrayList(orderCache.asMap().values());
    }

    @Override
    public List<Order> getOrdersByInstId(long instId) {
        return orderCache.asMap().values().stream().filter(order -> order.instId() == instId).collect(toList());
    }

    @Override
    public List<Order> getOrdersByPortfolioId(int portfolioId) {
        return orderCache.asMap().values().stream().filter(order -> portfolioId == order.portfolioId()).collect(toList());
    }

    @Override
    public List<Order> getOrdersByStrategyId(int strategyId) {
        return orderCache.asMap().values().stream().filter(order -> strategyId == order.strategyId()).collect(toList());
    }

    @Override
    public long nextId() {
        return idSupplier.next();
    }
}
