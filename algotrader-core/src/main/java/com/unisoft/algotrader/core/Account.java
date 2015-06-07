package com.unisoft.algotrader.core;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.series.DoubleTimeSeries;

import java.util.Map;

/**
 * Created by alex on 5/27/15.
 */
public class Account {

    private String name;
    private String description;

    private Currency currency;

    private Map<Currency, AccountPosition> accountPositions = Maps.newConcurrentMap();


    public Account(String name){
        this(name,"", CurrencyManager.DEFAULT_CURRENCY);
    }

    public Account(String name, Currency currency){
        this(name,"", currency);
    }

    public Account(String name, String description, Currency currency){
        this.name        = name;
        this.description = description;
        this.currency    = currency;
    }

    public Account(String name, String description, Currency currency, double initialValue){
        this.name        = name;
        this.description = description;
        this.currency    = currency;

        deposit(System.currentTimeMillis(), currency, initialValue, "Initial Deposit");
    }


    public Currency currency() {
        return currency;
    }

    public String description() {
        return description;
    }

    public String name() {
        return name;
    }

    public void add(AccountTransaction transaction){
        if (!accountPositions.containsKey(transaction.currency)){
            accountPositions.putIfAbsent(transaction.currency, new AccountPosition(transaction.currency));
        }
        AccountPosition accountPosition = accountPositions.get(transaction.currency);
        accountPosition.add(transaction);

    }

    public void deposit(long dateTime, Currency currency, double val, String text){
        add(new AccountTransaction(dateTime, currency, Math.abs(val),text));
    }

    public void withdraw(long dateTime, Currency currency, double val, String text){
        add(new AccountTransaction(dateTime, currency, -Math.abs(val),text));
    }

    public double value(Currency currency){
        if (accountPositions.containsKey(currency)){
            return accountPositions.get(currency).getValue();
        }
        return 0.0;
    }

    public double value() {
        double val = 0;

        for(AccountPosition position : accountPositions.values()) {
            val += CurrencyConverter.convert(position.getValue(), position.currency, currency);
        }

        return val;
    }

    public Map<Currency, Double> valueAsMap(){

        Map<Currency, Double> val = Maps.newHashMap();
        for(AccountPosition position : accountPositions.values()) {
            val.put(position.currency,position.getValue());
        }
        return val;
    }



}
