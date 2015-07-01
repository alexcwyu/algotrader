package com.unisoft.algotrader.core;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.Date;
import java.util.Map;

/**
 * Created by alex on 5/17/15.
 */
@Table("instrument")
public class Instrument{

    @PrimaryKey("inst_id")
    public int instId;
    public InstType type;
    public String name;
    public String symbol;
    @Column("exch_id")
    public String exchId;
    @Column("ccy_id")
    public String ccyId;

    @Column("und_inst_id")
    public int underlyingInstId;

    //derivative
    public double factor = 1;
    @Column("expiry_date")
    public Date expiryDate = null;
    public double strike = 0.0;
    @Column("put_call")
    public PutCall putCall = null;

    public double margin = 0;

    @Column("alt_symbols")
    public Map<String, String> altSymbols = Maps.newHashMap();

    @Column("alt_exchids")
    public Map<String, String> altExchIds = Maps.newHashMap();

    public String sector;
    public String group;


    public Instrument(){

    }

    public Instrument(int instId, InstType type, String symbol, String exchId, String ccyId){
        this(instId, type, symbol, symbol, exchId, ccyId);
    }

    public Instrument(int instId, InstType type, String name, String symbol, String exchId, String ccyId){

        this.instId = instId;
        this.type = type;
        this.name = name;
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
                Objects.equal(name, that.name) &&
                Objects.equal(symbol, that.symbol) &&
                Objects.equal(exchId, that.exchId) &&
                Objects.equal(ccyId, that.ccyId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instId, type, name, symbol, exchId, ccyId);
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "instId=" + instId +
                ", type=" + type +
                ", symbol='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", exchId='" + exchId + '\'' +
                ", ccyId='" + ccyId + '\'' +
                '}';
    }

    public void addAltSymbol(String providerId, String symbol){
        this.altSymbols.put(providerId, symbol);
    }

    public String getAltSymbol(String providerId){
        return this.altSymbols.get(providerId);
    }

    public void addAltExchId(String providerId, String exchId){
        this.altExchIds.put(providerId, exchId);
    }

    public String getAltExchId(String providerId){
        return this.altExchIds.get(providerId);
    }

//    /**
//     * Created by alex on 6/17/15.
//     */
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
//
//    public static class Classification{
//        public final String group;
//        public final String sector;
//
//        public Classification(String group, String sector) {
//            this.group = group;
//            this.sector = sector;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (!(o instanceof Classification)) return false;
//            Classification that = (Classification) o;
//            return Objects.equal(group, that.group) &&
//                    Objects.equal(sector, that.sector);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hashCode(group, sector);
//        }
//
//        @Override
//        public String toString() {
//            return "Classification{" +
//                    "group='" + group + '\'' +
//                    ", sector='" + sector + '\'' +
//                    '}';
//        }
//    }

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
