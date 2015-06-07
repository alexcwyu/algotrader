package com.unisoft.algotrader.core;

/**
 * Created by alex on 5/17/15.
 */
public class Currency {
    public final String ccyId;
    public final String name;

    public static Currency USD = new CurrencyBuilder().setCcyId("USD").setName("US Dollar").createCurrency();
    public static Currency HKD = new CurrencyBuilder().setCcyId("HKD").setName("HK Dollar").createCurrency();
    public static Currency CNY = new CurrencyBuilder().setCcyId("CNY").setName("Chinese Yuan Renminbi").createCurrency();
    public static Currency RUR = new CurrencyBuilder().setCcyId("RUR").setName("Russian Ruble").createCurrency();
    public static Currency AUD = new CurrencyBuilder().setCcyId("AUD").setName("Australian Dollar").createCurrency();
    public static Currency NZD = new CurrencyBuilder().setCcyId("NZD").setName("New Zealand Dollar").createCurrency();
    public static Currency CAD = new CurrencyBuilder().setCcyId("CAD").setName("Canadian Dollar").createCurrency();

    public static Currency GBP = new CurrencyBuilder().setCcyId("GBP").setName("British Pound").createCurrency();
    public static Currency EUR = new CurrencyBuilder().setCcyId("USD").setName("Euro").createCurrency();
    public static Currency JPY = new CurrencyBuilder().setCcyId("JPY").setName("Japanese Yen").createCurrency();
    public static Currency CHF = new CurrencyBuilder().setCcyId("CHF").setName("Swiss Franc").createCurrency();
    public static Currency SGD = new CurrencyBuilder().setCcyId("SGD").setName("Singapore Dollar").createCurrency();
    public static Currency KRW = new CurrencyBuilder().setCcyId("KRW").setName("Korean (South) Won").createCurrency();
    public static Currency INR = new CurrencyBuilder().setCcyId("KRW").setName("Indian Rupee").createCurrency();

    private Currency(String ccyId, String name) {
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
}
