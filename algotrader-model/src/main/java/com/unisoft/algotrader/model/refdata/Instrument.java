package com.unisoft.algotrader.model.refdata;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import org.msgpack.annotation.OrdinalEnum;

import java.util.Date;
import java.util.Map;

/**
 * Created by alex on 5/17/15.
 */
@Table(keyspace = "refdata", name = "instruments")
public class Instrument{

    @PartitionKey
    @Column(name ="inst_id")
    private long instId;

    private InstType type;

    private String name;

    private String symbol;

    @Column(name ="exch_id")
    private String exchId;

    @Column(name ="ccy_id")
    private String ccyId;

    @Column(name ="und_inst_id")
    private int underlyingInstId;

    //derivative
    private double factor = 1;

    @Column(name ="expiry_date")
    private Date expiryDate = null;

    private double strike = 0.0;

    @Column(name ="put_call")
    private PutCall putCall = null;

    private double margin = 0;

    @Column(name ="alt_symbols")
    private Map<String, String> altSymbols;

    @Column(name ="alt_exchids")
    private Map<String, String> altExchIds;

    private String sector;

    private String group;

    public Instrument(){
    }

//    public Instrument(long instId, InstType type, String symbol, String exchId, String ccyId){
//        this(instId, type, symbol, null, exchId, ccyId);
//    }

    public Instrument(long instId, InstType type, String symbol, String name, String exchId, String ccyId){
        this.instId = instId;
        this.type = type;
        this.symbol = symbol;
        this.name = name;
        this.exchId = exchId;
        this.ccyId = ccyId;
        this.altSymbols = Maps.newHashMap();
        this.altExchIds = Maps.newHashMap();
    }


    public Instrument(long instId, InstType type, String symbol, String name, String exchId, String ccyId, Map<String, String> altSymbols, Map<String, String> altExchIds){
        this.instId = instId;
        this.type = type;
        this.symbol = symbol;
        this.name = name;
        this.exchId = exchId;
        this.ccyId = ccyId;
        this.altSymbols = altSymbols;
        this.altExchIds = altExchIds;
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

    public void addAltExchId(String providerId, String exchId){
        this.altExchIds.put(providerId, exchId);
    }

//    /**
//     * Created by alex on 6/17/15.
//     */
    public static final class InstId {

        public String symbol;
        public String exchId;

        public final static String SEPARATOR = "@";

        protected InstId(){}

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

    @OrdinalEnum
    public static enum PutCall{
        Put,
        Call
    }

    @OrdinalEnum
    public static enum InstType{
        Stock,
        Future,
        Option,
        FutureOption,
        Index,
        FX,
        ETF,
        Combo
    }

    public long getInstId() {
        return instId;
    }

    public void setInstId(long instId) {
        this.instId = instId;
    }

    public InstType getType() {
        return type;
    }

    public void setType(InstType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchId() {
        return exchId;
    }

    public void setExchId(String exchId) {
        this.exchId = exchId;
    }

    public String getCcyId() {
        return ccyId;
    }

    public void setCcyId(String ccyId) {
        this.ccyId = ccyId;
    }

    public int getUnderlyingInstId() {
        return underlyingInstId;
    }

    public void setUnderlyingInstId(int underlyingInstId) {
        this.underlyingInstId = underlyingInstId;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getStrike() {
        return strike;
    }

    public void setStrike(double strike) {
        this.strike = strike;
    }

    public PutCall getPutCall() {
        return putCall;
    }

    public void setPutCall(PutCall putCall) {
        this.putCall = putCall;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public Map<String, String> getAltSymbols() {
        return altSymbols;
    }

    public void setAltSymbols(Map<String, String> altSymbols) {
        this.altSymbols = altSymbols;
    }

    public Map<String, String> getAltExchIds() {
        return altExchIds;
    }

    public void setAltExchIds(Map<String, String> altExchIds) {
        this.altExchIds = altExchIds;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getExchId(String providerId){
        if (altExchIds.containsKey(providerId)){
            return altExchIds.get(providerId);
        }
        return exchId;
    }

    public String getSymbol(String providerId){
        if (altSymbols.containsKey(providerId)){
            return altSymbols.get(providerId);
        }
        return symbol;
    }
}
