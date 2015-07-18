package com.unisoft.algotrader.provider;

import java.util.Objects;

/**
 * Created by alex on 18/7/15.
 */
public class HistoricalSubscriptionKey extends SubscriptionKey {
    public final long fromDate;
    public final long toDate;

    protected HistoricalSubscriptionKey(String providerId, Type type, long instId, long fromDate, long toDate){
        super(providerId, type, instId, 0);
        this.fromDate = fromDate;
        this.toDate = toDate;

    }

    protected HistoricalSubscriptionKey(String providerId, Type type, long instId, int barSize, long fromDate, long toDate){
        super(providerId, type, instId, barSize);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoricalSubscriptionKey)) return false;
        if (!super.equals(o)) return false;
        HistoricalSubscriptionKey that = (HistoricalSubscriptionKey) o;
        return Objects.equals(fromDate, that.fromDate) &&
                Objects.equals(toDate, that.toDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fromDate, toDate);
    }

    @Override
    public String toString() {
        return "HistoricalSubscriptionKey{" +
                "fromDate=" + fromDate +
                ", toDate=" + toDate +
                "} " + super.toString();
    }


}
