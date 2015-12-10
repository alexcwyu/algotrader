package com.unisoft.algotrader.persistence;

import com.unisoft.algotrader.model.trading.Account;
import com.unisoft.algotrader.model.trading.Portfolio;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by alex on 7/14/15.
 */
@Singleton
public class SampleInMemoryTradingDataStore  extends  InMemoryTradingDataStore{

    @Inject
    public SampleInMemoryTradingDataStore(){
        super();
        this.saveAccount(Account.TEST_USD_ACCOUNT);
        this.saveAccount(Account.TEST_HKD_ACCOUNT);
        this.savePortfolio(Portfolio.TEST_USD_PORTFOLIO);
        this.savePortfolio(Portfolio.TEST_HKD_PORTFOLIO);

    }
}
