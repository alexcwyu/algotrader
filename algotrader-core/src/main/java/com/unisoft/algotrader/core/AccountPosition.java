package com.unisoft.algotrader.core;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by alex on 5/30/15.
 */
public class AccountPosition {

    public final Currency currency;
    private double value;

    public List<AccountTransaction> transactions = Lists.newArrayList();

    public AccountPosition(Currency currency){
        this.currency = currency;
    }

    public void add(AccountTransaction transaction){
        if (transaction.currency != currency)
            throw new IllegalArgumentException("Currency not match, transaction.currency="+transaction.currency+", position.currency="+currency);
        this.transactions.add(transaction);
        this.value += transaction.value;
    }

    public double getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "AccountPosition{" +
                "currency=" + currency +
                ", value=" + value +
                ", transactions=" + transactions +
                '}';
    }
}
