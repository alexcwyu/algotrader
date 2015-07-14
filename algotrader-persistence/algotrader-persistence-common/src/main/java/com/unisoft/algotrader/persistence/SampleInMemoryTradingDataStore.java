package com.unisoft.algotrader.persistence;

/**
 * Created by alex on 7/14/15.
 */
public class SampleInMemoryTradingDataStore  extends  InMemoryTradingDataStore{

    public SampleInMemoryTradingDataStore(){
        super();
        this.saveAccount(TradingDataStore.DEFAULT_ACCOUNT);
    }
}
