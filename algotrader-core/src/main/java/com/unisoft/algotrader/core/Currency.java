package com.unisoft.algotrader.core;

import com.google.common.base.Objects;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by alex on 5/17/15.
 */

@Table("currency")
public class Currency{
    @PrimaryKey("ccy_id")
    private String ccyId;
    private String name;

    public static Currency USD = new CurrencyBuilder().setCcyId("USD").setName("US Dollar").createCurrency();
    public static Currency HKD = new CurrencyBuilder().setCcyId("HKD").setName("HK Dollar").createCurrency();


    public static Currency CNY = new CurrencyBuilder().setCcyId("CNY").setName("Chinese Yuan Renminbi").createCurrency();
    public static Currency RUR = new CurrencyBuilder().setCcyId("RUR").setName("Russian Ruble").createCurrency();
    public static Currency AUD = new CurrencyBuilder().setCcyId("AUD").setName("Australian Dollar").createCurrency();
    public static Currency NZD = new CurrencyBuilder().setCcyId("NZD").setName("New Zealand Dollar").createCurrency();
    public static Currency CAD = new CurrencyBuilder().setCcyId("CAD").setName("Canadian Dollar").createCurrency();

    public static Currency GBP = new CurrencyBuilder().setCcyId("GBP").setName("British Pound").createCurrency();
    public static Currency EUR = new CurrencyBuilder().setCcyId("EUR").setName("Euro").createCurrency();
    public static Currency JPY = new CurrencyBuilder().setCcyId("JPY").setName("Japanese Yen").createCurrency();
    public static Currency CHF = new CurrencyBuilder().setCcyId("CHF").setName("Swiss Franc").createCurrency();
    public static Currency SGD = new CurrencyBuilder().setCcyId("SGD").setName("Singapore Dollar").createCurrency();
    public static Currency KRW = new CurrencyBuilder().setCcyId("KRW").setName("Korean (South) Won").createCurrency();
    public static Currency INR = new CurrencyBuilder().setCcyId("KRW").setName("Indian Rupee").createCurrency();

    public Currency(String ccyId, String name) {
        super();
        this.ccyId = ccyId;
        this.name = name;

        CurrencyManager.INSTANCE.add(this);
    }

    public static class CurrencyBuilder {
        private String ccyId;
        private String name;

        public CurrencyBuilder setCcyId(String ccyId) {
            this.ccyId = ccyId;
            return this;
        }

        public CurrencyBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public Currency createCurrency() {
            return new Currency(ccyId, name);
        }
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
