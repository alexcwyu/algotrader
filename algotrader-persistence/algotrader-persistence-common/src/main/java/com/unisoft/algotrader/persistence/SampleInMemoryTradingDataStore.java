package com.unisoft.algotrader.persistence;

import javax.inject.Singleton;

/**
 * Created by alex on 7/14/15.
 */
@Singleton
public class SampleInMemoryTradingDataStore  extends  InMemoryTradingDataStore{

    public SampleInMemoryTradingDataStore(){
        super();
        this.saveAccount(TradingDataStore.DEFAULT_ACCOUNT);
    }
}
