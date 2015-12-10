package com.unisoft.algotrader.demo;

import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.model.trading.Account;
import com.unisoft.algotrader.model.trading.Portfolio;
import com.unisoft.algotrader.provider.ProviderId;

import java.util.Date;

/**
 * Created by alex on 11/30/15.
 */
public class BackTestConfig extends TradingConfig{

    public static final Currency DEFAULT_CURRENCY = Currency.USD;
    public static final int DEFAULT_INIT_WEALTH = 100000;


    public final Date fromDate;
    public final Date toDate;
    public final String accountId;
    public final Currency accountCurrency;
    public final int initWealth;
    public final int portfolioId;


    public BackTestConfig(int marketDataProviderId, int strategyId,
                          Date fromDate, Date toDate, String accountId, Currency accountCurrency, int initWealth, int portfolioId, long ... instIds) {
        super(marketDataProviderId, ProviderId.Simulation.id, strategyId, instIds);
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.accountId = accountId;
        this.accountCurrency = accountCurrency;
        this.initWealth = initWealth;
        this.portfolioId = portfolioId;
    }


    public BackTestConfig(int marketDataProviderId, int strategyId,
                          Date fromDate, Date toDate, long ... instIds) {
        this(marketDataProviderId, strategyId, fromDate, toDate, Account.TEST_USD_ACCOUNT_ID, DEFAULT_CURRENCY, DEFAULT_INIT_WEALTH, Portfolio.TEST_USD_PORTFOLIO_ID, instIds);
    }
}
