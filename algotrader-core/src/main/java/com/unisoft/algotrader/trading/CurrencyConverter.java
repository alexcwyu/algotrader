package com.unisoft.algotrader.trading;

import com.unisoft.algotrader.model.refdata.Currency;

/**
 * Created by alex on 5/30/15.
 */
public class CurrencyConverter {


    public static double convert(double amount, Currency fromCurrency, Currency toCurrency){
        if (fromCurrency == toCurrency){
            return amount;
        }
        throw new UnsupportedOperationException();
    }

    public static double convert(double amount, Currency fromCurrency, Currency toCurrency, long dateTime){
        if (fromCurrency == toCurrency){
            return amount;
        }
        throw new UnsupportedOperationException();
    }

}
