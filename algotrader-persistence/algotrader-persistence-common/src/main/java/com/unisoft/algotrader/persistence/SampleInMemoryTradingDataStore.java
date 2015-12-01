package com.unisoft.algotrader.persistence;

import com.unisoft.algotrader.model.trading.Account;
import com.unisoft.algotrader.model.trading.Portfolio;

import javax.inject.Singleton;

/**
 * Created by alex on 7/14/15.
 */
@Singleton
public class SampleInMemoryTradingDataStore  extends  InMemoryTradingDataStore{

    public SampleInMemoryTradingDataStore(){
        super();
        this.saveAccount(Account.DEFAULT);
        this.savePortfolio(Portfolio.DEFAULT);
    }
}
