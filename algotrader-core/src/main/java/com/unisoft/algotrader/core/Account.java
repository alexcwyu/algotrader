package com.unisoft.algotrader.core;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.FrozenValue;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alex on 5/27/15.
 */

@Table(keyspace = "trading", name = "accounts")
public class Account {
    @PartitionKey
    private String name;

    private String description;

    @Column(name ="ccy_id")
    private String ccyId;

    @Column(name ="positions")
    @FrozenValue
    private Map<String, AccountPosition> accountPositions = Maps.newHashMap();

    public Account(){

    }

    public Account(String name){
        this(name,"", CurrencyManager.DEFAULT_CURRENCY);
    }

    public Account(String name, Currency currency){
        this(name,"", currency);
    }

    public Account(String name, String description, Currency currency){
        this.name        = name;
        this.description = description;
        this.ccyId    = currency.getCcyId();
    }

    public Account(String name, String description, Currency currency, double initialValue){
        this.name        = name;
        this.description = description;
        this.ccyId    = currency.getCcyId();

        deposit(System.currentTimeMillis(), currency, initialValue, "Initial Deposit");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCcyId(String ccyId) {
        this.ccyId = ccyId;
    }

    public void setAccountPositions(Map<String, AccountPosition> accountPositions) {
        this.accountPositions = accountPositions;
    }

    public String getCcyId() {
        return ccyId;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Map<String, AccountPosition> getAccountPositions() {
        return accountPositions;
    }

    public void add(AccountTransaction transaction){
        if (!accountPositions.containsKey(transaction.getCcyId())){
            accountPositions.putIfAbsent(transaction.getCcyId(), new AccountPosition(transaction.getCcyId()));
        }
        AccountPosition accountPosition = accountPositions.get(transaction.getCcyId());
        accountPosition.add(transaction);

    }

    public void deposit(long dateTime, Currency currency, double val, String text){
        add(new AccountTransaction(dateTime, currency, Math.abs(val),text));
    }

    public void withdraw(long dateTime, Currency currency, double val, String text){
        add(new AccountTransaction(dateTime, currency, -Math.abs(val),text));
    }

    public double value(Currency currency){
        if (accountPositions.containsKey(currency.getCcyId())){
            return accountPositions.get(currency.getCcyId()).getValue();
        }
        return 0.0;
    }

    public double value() {
        double val = 0;

        for(AccountPosition position : accountPositions.values()) {
            val += CurrencyConverter.convert(position.getValue(), position.getCcyId(), ccyId);
        }

        return val;
    }

    public Map<String, Double> valueAsMap(){

        Map<String, Double> val = Maps.newHashMap();
        for(AccountPosition position : accountPositions.values()) {
            val.put(position.getCcyId(),position.getValue());
        }
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equal(name, account.name) &&
                Objects.equal(description, account.description) &&
                Objects.equal(ccyId, account.ccyId) &&
                Objects.equal(accountPositions, account.accountPositions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, description, ccyId, accountPositions);
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", currency=" + ccyId +
                ", accountPositions=" + accountPositions +
                '}';
    }
}
