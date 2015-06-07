package com.unisoft.algotrader.core;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 5/27/15.
 */
public class CurrencyManager {

    public static final CurrencyManager INSTANCE;


    static {
        INSTANCE = new CurrencyManager();
    }

    public static final Currency DEFAULT_CURRENCY = Currency.USD;

    private CurrencyManager(){
    }

    private Map<String, Currency> map = Maps.newConcurrentMap();

    public void add(Currency currency){
        map.put(currency.ccyId, currency);
    }

    public Currency get(String ccyId){
        return map.get(ccyId);
    }
}
