package com.unisoft.algotrader.refdata;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.refdata.Currency;

import java.util.Map;

/**
 * Created by alex on 5/27/15.
 */
public class CurrencyManager {

    public static final CurrencyManager INSTANCE;


    static {
        INSTANCE = new CurrencyManager();
        INSTANCE.add(Currency.USD);
        INSTANCE.add(Currency.HKD);

        INSTANCE.add(Currency.CNY);
        INSTANCE.add(Currency.RUR);
        INSTANCE.add(Currency.AUD);
        INSTANCE.add(Currency.NZD);
        INSTANCE.add(Currency.CAD);

        INSTANCE.add(Currency.GBP);
        INSTANCE.add(Currency.EUR);
        INSTANCE.add(Currency.JPY);
        INSTANCE.add(Currency.CHF);
        INSTANCE.add(Currency.SGD);
        INSTANCE.add(Currency.KRW);
        INSTANCE.add(Currency.INR);
    }

    public static final Currency DEFAULT_CURRENCY = Currency.USD;

    private CurrencyManager(){
    }

    private Map<String, Currency> map = Maps.newConcurrentMap();

    public void add(Currency currency){
        map.put(currency.getCcyId(), currency);
    }

    public Currency get(String ccyId){
        return map.get(ccyId);
    }
}
