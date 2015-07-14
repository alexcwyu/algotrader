package com.unisoft.algotrader.persistence;

import com.unisoft.algotrader.model.refdata.Currency;

/**
 * Created by alex on 7/14/15.
 */
public class SampleInMemoryRefDataStore extends InMemoryRefDataStore {
    public SampleInMemoryRefDataStore(){
        super();
        this.saveCurrency(Currency.USD);
        this.saveCurrency(Currency.HKD);

        this.saveCurrency(Currency.CNY);
        this.saveCurrency(Currency.RUR);
        this.saveCurrency(Currency.AUD);
        this.saveCurrency(Currency.NZD);
        this.saveCurrency(Currency.CAD);

        this.saveCurrency(Currency.GBP);
        this.saveCurrency(Currency.EUR);
        this.saveCurrency(Currency.JPY);
        this.saveCurrency(Currency.CHF);
        this.saveCurrency(Currency.SGD);
        this.saveCurrency(Currency.KRW);
        this.saveCurrency(Currency.INR);
    }
}
