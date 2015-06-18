package com.unisoft.algotrader.core.id;

import com.google.common.base.Objects;

/**
 * Created by alex on 6/17/15.
 */
public final class InstId {

    public static final String SEPERATOR = "@";

    public final String symbol;
    public final String exchId;

    private InstId(InstId.Builder builder) {
        this.symbol = builder.symbol;
        this.exchId = builder.exchId;
    }


    private InstId(String symbol, String exchId) {
        this.symbol = symbol;
        this.exchId = exchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstId)) return false;
        InstId instId = (InstId) o;
        return Objects.equal(symbol, instId.symbol) &&
                Objects.equal(exchId, instId.exchId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(symbol, exchId);
    }

    @Override
    public String toString() {
        return symbol+SEPERATOR+exchId;
    }


    public static class Builder {
        private String symbol;
        private String exchId;

        public static Builder as(){
            return new Builder();
        }

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder exchId(String exchId) {
            this.exchId = exchId;
            return this;
        }

        public InstId build() {
            return new InstId(this);
        }
    }

    public static InstId parse(String id){
        String [] token = id.split(SEPERATOR);
        if (token.length != 2){
            throw new IllegalArgumentException(String.format("Unknown format %1$s", id));
        }
        return new InstId(token[0], token[1]);
    }
}
