package com.unisoft.algotrader.core;


import com.google.common.base.Objects;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by alex on 5/17/15.
 */
@Table("exchange")
public class Exchange{
    @PrimaryKey("exch_id")
    private String exchId;
    private String name;

    public Exchange(String exchId, String name) {
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

    public String getExchId() {
        return exchId;
    }

    public void setExchId(String exchId) {
        this.exchId = exchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exchange)) return false;
        Exchange exchange = (Exchange) o;
        return Objects.equal(exchId, exchange.exchId) &&
                Objects.equal(name, exchange.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(exchId, name);
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "exchId='" + exchId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
