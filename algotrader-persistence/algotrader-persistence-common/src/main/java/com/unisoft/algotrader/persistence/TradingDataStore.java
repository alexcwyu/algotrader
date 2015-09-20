package com.unisoft.algotrader.persistence;

import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.trading.Account;
import com.unisoft.algotrader.model.trading.Portfolio;

import java.util.List;

/**
 * Created by alex on 7/7/15.
 */
public interface TradingDataStore {

    public static final Account DEFAULT_ACCOUNT = new Account("Test", "Testing Account", Currency.USD, 1000000);

    void connect();

    void saveAccount(Account account);

    Account getAccount(String accountId);

    List<Account> getAllAccounts();

    void savePortfolio(Portfolio portfolio);

    Portfolio getPortfolio(int portfolioId);

    List<Portfolio> getAllPortfolios();

    void saveExecutionReport(ExecutionReport er);

    ExecutionReport getExecutionReport(long execId);

    List<ExecutionReport> getAllExecutionReports();

    List<ExecutionReport> getExecutionReportsByInstId(long instId);

    List<ExecutionReport> getExecutionReportsByOrderId(long clOrderId);

    void saveOrder(Order order);

    Order getOrder(long clOrderId);

    List<Order> getAllOrders();

    List<Order> getOrdersByInstId(long instId);

    List<Order> getOrdersByPortfolioId(int portfolioId);

    List<Order> getOrdersByStrategyId(int strategyId);

    long nextId();
}
