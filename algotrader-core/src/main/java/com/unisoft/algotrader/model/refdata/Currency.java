package com.unisoft.algotrader.model.refdata;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.base.Objects;

/**
 * Created by alex on 5/17/15.
 */
@Table(keyspace = "refdata", name = "currencies")
public class Currency{

    @PartitionKey
    @Column(name ="ccy_id" )
    private String ccyId;
    private String name;

    public static Currency USD = new Currency("USD","US Dollar");
    public static Currency HKD = new Currency("HKD","HK Dollar");


    public static Currency CNY = new Currency("CNY","Chinese Yuan Renminbi");
    public static Currency RUR = new Currency("RUR","Russian Ruble");
    public static Currency AUD = new Currency("AUD","Australian Dollar");
    public static Currency NZD = new Currency("NZD","New Zealand Dollar");
    public static Currency CAD = new Currency("CAD","Canadian Dollar");

    public static Currency GBP = new Currency("GBP","British Pound");
    public static Currency EUR = new Currency("EUR","Euro");
    public static Currency JPY = new Currency("JPY","Japanese Yen");
    public static Currency CHF = new Currency("CHF","Swiss Franc");
    public static Currency SGD = new Currency("SGD","Singapore Dollar");
    public static Currency KRW = new Currency("KRW","Korean (South) Won");
    public static Currency INR = new Currency("KRW","Indian Rupee");

    public Currency(){
    }

    public Currency(String ccyId, String name) {
        super();
        this.ccyId = ccyId;
        this.name = name;

        CurrencyManager.INSTANCE.add(this);
    }

    @Override
    public String toString() {
        return "Currency{" +
                "ccyId='" + ccyId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;
        Currency currency = (Currency) o;
        return Objects.equal(ccyId, currency.ccyId) &&
                Objects.equal(name, currency.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ccyId, name);
    }


    public String getCcyId() {
        return ccyId;
    }

    public String getName() {
        return name;
    }

    public void setCcyId(String ccyId) {
        this.ccyId = ccyId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
