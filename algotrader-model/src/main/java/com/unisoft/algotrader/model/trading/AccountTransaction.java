package com.unisoft.algotrader.model.trading;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;
import com.google.common.base.Objects;
import com.unisoft.algotrader.model.refdata.Currency;

/**
 * Created by alex on 5/29/15.
 */

@UDT(name = "account_transaction", keyspace = "trading")
public class AccountTransaction {

    @Field(name = "cl_order_id")
    private long clOrderId;

    private long datetime;

    @Field(name = "ccy_id")
    private String ccyId;

    private double value;

    private String text;

    public AccountTransaction(){

    }

    public AccountTransaction(Currency currency, double value){
        this(System.currentTimeMillis(), currency.getCcyId(), value, "");
    }

    public AccountTransaction(long clOrderId, long datetime, Currency currency, double value, String text){
        this(clOrderId, datetime, currency.getCcyId(), value, text);
    }

    public AccountTransaction(long clOrderId, long datetime, String ccyId, double value, String text){
        this.clOrderId = clOrderId;
        this.datetime = datetime;
        this.ccyId = ccyId;
        this.value = value;
        this.text = text;
    }

    public AccountTransaction(long datetime, Currency currency, double value, String text){
        this(datetime, currency.getCcyId(), value, text);
    }

    public AccountTransaction(long datetime, String ccyId, double value, String text){
        this.datetime = datetime;
        this.ccyId = ccyId;
        this.value = value;
        this.text = text;
    }

    @Override
    public String toString() {
        return "AccountTransaction{" +
                ", getDatetime=" + datetime +
                ", currency=" + ccyId +
                ", getValue=" + value +
                ", getClOrderId=" + clOrderId +
                ", getText='" + text + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountTransaction)) return false;
        AccountTransaction that = (AccountTransaction) o;
        return Objects.equal(clOrderId, that.clOrderId) &&
                Objects.equal(datetime, that.datetime) &&
                Objects.equal(value, that.value) &&
                Objects.equal(ccyId, that.ccyId) &&
                Objects.equal(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clOrderId, datetime, ccyId, value, text);
    }

    public String ccyId() {
        return ccyId;
    }

    public AccountTransaction ccyId(String ccyId) {
        this.ccyId = ccyId;
        return this;
    }

    public long clOrderId() {
        return clOrderId;
    }

    public AccountTransaction clOrderId(long clOrderId) {
        this.clOrderId = clOrderId;
        return this;
    }

    public long datetime() {
        return datetime;
    }

    public AccountTransaction datetime(long datetime) {
        this.datetime = datetime;
        return this;
    }

    public String text() {
        return text;
    }

    public AccountTransaction text(String text) {
        this.text = text;
        return this;
    }

    public double value() {
        return value;
    }

    public AccountTransaction value(double value) {
        this.value = value;
        return this;
    }
}
