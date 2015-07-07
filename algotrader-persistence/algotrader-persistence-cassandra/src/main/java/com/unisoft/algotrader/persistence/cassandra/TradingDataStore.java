package com.unisoft.algotrader.persistence.cassandra;

import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.trading.Account;
import com.unisoft.algotrader.model.trading.Portfolio;

/**
 * Created by alex on 7/7/15.
 */
public interface TradingDataStore {

    void saveAccount(Account account);

    Account getAccount(String accountId);

    void savePortfolio(Portfolio portfolio);

    Portfolio getPortfolio(String portfolioId);

    void saveExecutionReport(ExecutionReport er);

    ExecutionReport getExecutionReport(long execId);

    void saveOrder(Order order);

    Order getOrder(long orderId);
}
