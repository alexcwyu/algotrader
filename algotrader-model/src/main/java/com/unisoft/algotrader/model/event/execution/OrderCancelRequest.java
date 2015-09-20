package com.unisoft.algotrader.model.event.execution;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.google.common.base.Objects;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.trading.Side;

/**
 * Created by alex on 5/24/15.
 */
@Deprecated
public class OrderCancelRequest <E extends OrderCancelRequest<? super E>> implements Event<OrderEventHandler, E> {

    @PartitionKey
    @Column(name="cl_order_id")
    public long clOrderId;

    @Column(name="order_id")
    public long orderId = -1;

    @Column(name="orig_cl_order_id")
    public long origClOrderId;

    @Column(name="inst_id")
    public long instId;

    @Column(name="date_time")
    public long dateTime;

    public Side side;

    @Column(name="ord_qty")
    public double ordQty;

    @Column(name="exec_provider_id")
    public String execProviderId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderCancelRequest)) return false;
        OrderCancelRequest<?> that = (OrderCancelRequest<?>) o;
        return Objects.equal(clOrderId, that.clOrderId) &&
                Objects.equal(orderId, that.orderId) &&
                Objects.equal(origClOrderId, that.origClOrderId) &&
                Objects.equal(instId, that.instId) &&
                Objects.equal(dateTime, that.dateTime) &&
                Objects.equal(ordQty, that.ordQty) &&
                Objects.equal(side, that.side) &&
                Objects.equal(execProviderId, that.execProviderId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clOrderId, orderId, origClOrderId, instId, dateTime, side, ordQty, execProviderId);
    }

    @Override
    public String toString() {
        return "OrderCancelRequest{" +
                "clOrderId=" + clOrderId +
                ", orderId=" + orderId +
                ", origClOrderId=" + origClOrderId +
                ", instId=" + instId +
                ", dateTime=" + dateTime +
                ", side=" + side +
                ", ordQty=" + ordQty +
                ", providerId=" + execProviderId +
                '}';
    }

    @Override
    public void on(OrderEventHandler handler) {

    }

    public long clOrderId() {
        return clOrderId;
    }

    public void clOrderId(long clOrderId) {
        this.clOrderId = clOrderId;
    }

    public long dateTime() {
        return dateTime;
    }

    public void dateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String execProviderId() {
        return execProviderId;
    }

    public void execProviderId(String execProviderId) {
        this.execProviderId = execProviderId;
    }

    public long instId() {
        return instId;
    }

    public void instId(long instId) {
        this.instId = instId;
    }

    public long orderId() {
        return orderId;
    }

    public void orderId(long orderId) {
        this.orderId = orderId;
    }

    public double ordQty() {
        return ordQty;
    }

    public void ordQty(double ordQty) {
        this.ordQty = ordQty;
    }

    public long origClOrderId() {
        return origClOrderId;
    }

    public void origClOrderId(long origClOrderId) {
        this.origClOrderId = origClOrderId;
    }

    public Side side() {
        return side;
    }

    public void side(Side side) {
        this.side = side;
    }
}
