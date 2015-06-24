package com.unisoft.algotrader.core;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.Map;

/**
 * Created by alex on 5/17/15.
 */
public class Instrument {

    public final int instId;
    public final InstType type;
    public final String symbol;
    public final String exchId;
    public final String ccyId;

    public int underlyingInstId;

    //derivative
    public double factor = 1;
    public Date expiryDate = null;
    public double strike = 0.0;
    public PutCall putCall = null;

    public double margin = 0;

    public Map<String, InstId> altIds = Maps.newHashMap();
    public Map<String, Classification> classifications = Maps.newHashMap();


    public Instrument(int instId, InstType type, String symbol, String exchId, String ccyId){

        this.instId = instId;
        this.type = type;
        this.symbol = symbol;
        this.exchId = exchId;
        this.ccyId = ccyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instrument)) return false;
        Instrument that = (Instrument) o;
        return Objects.equal(instId, that.instId) &&
                Objects.equal(type, that.type) &&
                Objects.equal(symbol, that.symbol) &&
                Objects.equal(exchId, that.exchId) &&
                Objects.equal(ccyId, that.ccyId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instId, type, symbol, exchId, ccyId);
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "instId=" + instId +
                ", type=" + type +
                ", symbol='" + symbol + '\'' +
                ", exchId='" + exchId + '\'' +
                ", ccyId='" + ccyId + '\'' +
                '}';
    }

    public void addAltId(String providerId, InstId altInstId){
        this.altIds.put(providerId, altInstId);
    }

    public InstId getAltId(String providerId){
        return this.altIds.get(providerId);
    }

    /**
     * Created by alex on 6/17/15.
     */
    public static final class InstId {

        public final String symbol;
        public final String exchId;

        public final static String SEPARATOR = "@";

        public InstId(String symbol, String exchId) {
            this.symbol = symbol;
            this.exchId = exchId;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof InstId)) return false;
            InstId altInstId = (InstId) o;
            return Objects.equal(symbol, altInstId.symbol) &&
                    Objects.equal(exchId, altInstId.exchId);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(symbol, exchId);
        }

        @Override
        public String toString() {
            return symbol + SEPARATOR + exchId;
        }
    }

    public static class Classification{
        public final String group;
        public final String sector;

        public Classification(String group, String sector) {
            this.group = group;
            this.sector = sector;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Classification)) return false;
            Classification that = (Classification) o;
            return Objects.equal(group, that.group) &&
                    Objects.equal(sector, that.sector);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(group, sector);
        }

        @Override
        public String toString() {
            return "Classification{" +
                    "group='" + group + '\'' +
                    ", sector='" + sector + '\'' +
                    '}';
        }
    }

    public static enum PutCall{
        Put,
        Call
    }

    public static enum InstType{
        Stock,
        Future,
        Option,
        Index,
        FX,
        ETF,
    }
}
