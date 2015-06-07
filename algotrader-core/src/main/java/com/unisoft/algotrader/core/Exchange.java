package com.unisoft.algotrader.core;

/**
 * Created by alex on 5/17/15.
 */
public class Exchange {

    public final String exchId;
    public final String name;

    private Exchange(String exchId, String name) {
        this.exchId = exchId;
        this.name = name;
    }

    public static class ExchangeBuilder {
        private String exchId;
        private String name;

        public ExchangeBuilder setExchId(String exchId) {
            this.exchId = exchId;
            return this;
        }

        public ExchangeBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public Exchange createExchange() {
            return new Exchange(exchId, name);
        }
    }
}
