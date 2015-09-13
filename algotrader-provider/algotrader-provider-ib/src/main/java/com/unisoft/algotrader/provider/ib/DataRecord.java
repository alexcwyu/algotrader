package com.unisoft.algotrader.provider.ib;

import com.google.common.base.Objects;

/**
 * Created by alex on 9/14/15.
 */
public class DataRecord {

    protected final long instId;
    protected double bid = 0.0;
    protected double ask = 0.0;
    protected double last = 0.0;
    protected double high = 0.0;
    protected double low = 0.0;
    protected double close = 0.0;

    protected int bidSize = 0;
    protected int askSize = 0;
    protected int lastSize = 0;

    protected int volume = 0;

    protected boolean tradeRequested = false;
    protected boolean quoteRequested = false;

    public DataRecord(final long instId){
        this.instId = instId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataRecord)) return false;
        DataRecord that = (DataRecord) o;
        return Objects.equal(instId, that.instId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instId);
    }

    @Override
    public String toString() {
        return "DataRecord{" +
                "instId=" + instId +
                ", bid=" + bid +
                ", ask=" + ask +
                ", last=" + last +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", bidSize=" + bidSize +
                ", askSize=" + askSize +
                ", lastSize=" + lastSize +
                ", volume=" + volume +
                ", tradeRequested=" + tradeRequested +
                ", quoteRequested=" + quoteRequested +
                '}';
    }
}
