package com.unisoft.algotrader.model.id;

import com.google.common.base.Objects;

/**
 * Created by alex on 9/20/15.
 */
public class ClOrderId {

    public final int strategyId;

    public final long orderId;

    public ClOrderId(int strategyId, long orderId) {
        this.strategyId = strategyId;
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClOrderId)) return false;
        ClOrderId clOrderId = (ClOrderId) o;
        return Objects.equal(strategyId, clOrderId.strategyId) &&
                Objects.equal(orderId, clOrderId.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(strategyId, orderId);
    }

    @Override
    public String toString() {
        return "ClOrderId{" +
                "orderId=" + orderId +
                ", strategyId=" + strategyId +
                '}';
    }

    public long orderId() {
        return orderId;
    }

    public int strategyId() {
        return strategyId;
    }
}
