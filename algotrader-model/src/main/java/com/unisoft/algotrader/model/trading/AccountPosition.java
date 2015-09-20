package com.unisoft.algotrader.model.trading;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.FrozenValue;
import com.datastax.driver.mapping.annotations.UDT;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.refdata.Currency;

import java.util.List;

/**
 * Created by alex on 5/30/15.
 */
@UDT(name = "account_position", keyspace = "trading")
public class AccountPosition {

    @Field(name = "ccy_id")
    private String ccyId;

    private double value;

    @FrozenValue
    @Field(name = "transactions")
    private List<AccountTransaction> accountTransactions = Lists.newArrayList();

    public AccountPosition(){}

    public AccountPosition(String ccyId){
        this.ccyId = ccyId;
    }

    public AccountPosition(Currency currency){
        this.ccyId = currency.getCcyId();
    }

    public void add(AccountTransaction transaction){
        if (transaction.ccyId() != ccyId)
            throw new IllegalArgumentException("Currency not match, transaction.currency="+transaction.ccyId()+", position.currency="+ccyId);
        this.accountTransactions.add(transaction);
        this.value += transaction.value();
    }


    @Override
    public String toString() {
        return "AccountPosition{" +
                "currency=" + ccyId +
                ", getValue=" + value +
                ", getAccountTransactions=" + accountTransactions +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountPosition)) return false;
        AccountPosition position = (AccountPosition) o;
        return Objects.equal(value, position.value) &&
                Objects.equal(ccyId, position.ccyId) &&
                Objects.equal(accountTransactions, position.accountTransactions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ccyId, value, accountTransactions);
    }

    public List<AccountTransaction> accountTransactions() {
        return accountTransactions;
    }

    public AccountPosition accountTransactions(List<AccountTransaction> accountTransactions) {
        this.accountTransactions = accountTransactions;
        return this;
    }

    public String ccyId() {
        return ccyId;
    }

    public AccountPosition ccyId(String ccyId) {
        this.ccyId = ccyId;
        return this;
    }

    public double value() {
        return value;
    }

    public AccountPosition value(double value) {
        this.value = value;
        return this;
    }
}
