package com.unisoft.algotrader.model.id;

import com.google.common.base.Objects;

/**
 * Created by alex on 9/20/15.
 */
public class OrderId {
    public final int providerId;
    public final long orderId;

    public OrderId(int providerId, long orderId) {
        this.providerId = providerId;
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderId)) return false;
        OrderId orderId1 = (OrderId) o;
        return Objects.equal(providerId, orderId1.providerId) &&
                Objects.equal(orderId, orderId1.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(providerId, orderId);
    }

    @Override
    public String toString() {
        return "OrderId{" +
                "orderId=" + orderId +
                ", providerId=" + providerId +
                '}';
    }

    public long orderId() {
        return orderId;
    }

    public int providerId() {
        return providerId;
    }
}
