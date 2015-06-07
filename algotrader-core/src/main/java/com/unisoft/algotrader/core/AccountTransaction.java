package com.unisoft.algotrader.core;

/**
 * Created by alex on 5/29/15.
 */
public class AccountTransaction {
    public int accountTransactionid;
    public long datetime;
    public Currency currency;
    public double value;

    public long transcationId;
    public long orderId;

    public String text;

    public AccountTransaction(){

    }

    public AccountTransaction(long datetime, Currency currency, double value, String text){
        this.currency = currency;
        this.value = value;
        this.text = text;
    }


    public AccountTransaction(long datetime, Currency currency, double value){
        this(datetime, currency, value, "");
    }

    public AccountTransaction(Currency currency, double value){
        this(System.currentTimeMillis(), currency, value, "");
    }

    public AccountTransaction(Currency currency, double value, String text){
        this(System.currentTimeMillis(), currency, value, text);
    }

    @Override
    public String toString() {
        return "AccountTransaction{" +
                "accountTransactionid=" + accountTransactionid +
                ", datetime=" + datetime +
                ", currency=" + currency +
                ", value=" + value +
                ", transcationId=" + transcationId +
                ", orderId=" + orderId +
                ", text='" + text + '\'' +
                '}';
    }
}
