package com.unisoft.algotrader.provider.data;

import com.unisoft.algotrader.model.event.data.DataType;

/**
 * Created by alex on 18/7/15.
 */
public class MarketDepthSubscriptionKey extends SubscriptionKey {
    public final int numRows;

    protected MarketDepthSubscriptionKey(String providerId, long instId, int numRows){
        super(providerId, DataType.MarketDepth, instId, 0);
        this.numRows = numRows;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketDepthSubscriptionKey)) return false;
        if (!super.equals(o)) return false;
        MarketDepthSubscriptionKey that = (MarketDepthSubscriptionKey) o;
        return com.google.common.base.Objects.equal(numRows, that.numRows);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(super.hashCode(), numRows);
    }

    @Override
    public String toString() {
        return "MarketDepthSubscriptionKey{" +
                "numRows=" + numRows +
                "} " + super.toString();
    }

    public static MarketDepthSubscriptionKey createSubscriptionKey(String providerId, long instId, int numRows){
        return new MarketDepthSubscriptionKey(providerId, instId, numRows);
    }

}
