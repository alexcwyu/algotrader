package com.unisoft.algotrader.model.event.execution;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.google.common.base.Objects;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.model.trading.TimeInForce;

/**
 * Created by alex on 5/24/15.
 */
public class OrderCancelReplaceRequest<E extends OrderCancelReplaceRequest<? super E>> implements Event<OrderHandler, E> {

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

    @Column(name="ord_type")
    public OrdType ordType;

    @Column(name="limit_price")
    public double limitPrice;

    @Column(name="stop_price")
    public double stopPrice;

    @Column(name="ord_qty")
    public double ordQty;

    public TimeInForce tif;

    public Side side;

    @Column(name="account")
    public String account;

    public String text;

    @Column(name="exec_provider_id")
    public String execProviderId;

    @Override
    public void copy(E event) {

    }

    @Override
    public void on(OrderHandler handler) {

    }

    @Override
    public void reset() {

    }

    public String account() {
        return account;
    }

    public OrderCancelReplaceRequest account(String account) {
        this.account = account;
        return this;
    }

    public long clOrderId() {
        return clOrderId;
    }

    public OrderCancelReplaceRequest clOrderId(long clOrderId) {
        this.clOrderId = clOrderId;
        return this;
    }

    public long dateTime() {
        return dateTime;
    }

    public OrderCancelReplaceRequest dateTime(long dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public long instId() {
        return instId;
    }

    public OrderCancelReplaceRequest instId(long instId) {
        this.instId = instId;
        return this;
    }

    public double limitPrice() {
        return limitPrice;
    }

    public OrderCancelReplaceRequest limitPrice(double limitPrice) {
        this.limitPrice = limitPrice;
        return this;
    }

    public long orderId() {
        return orderId;
    }

    public OrderCancelReplaceRequest orderId(long orderId) {
        this.orderId = orderId;
        return this;
    }

    public double ordQty() {
        return ordQty;
    }

    public OrderCancelReplaceRequest ordQty(double ordQty) {
        this.ordQty = ordQty;
        return this;
    }

    public OrdType ordType() {
        return ordType;
    }

    public OrderCancelReplaceRequest ordType(OrdType ordType) {
        this.ordType = ordType;
        return this;
    }

    public long origClOrderId() {
        return origClOrderId;
    }

    public OrderCancelReplaceRequest origClOrderId(long origClOrderId) {
        this.origClOrderId = origClOrderId;
        return this;
    }

    public Side side() {
        return side;
    }

    public OrderCancelReplaceRequest side(Side side) {
        this.side = side;
        return this;
    }

    public double stopPrice() {
        return stopPrice;
    }

    public OrderCancelReplaceRequest stopPrice(double stopPrice) {
        this.stopPrice = stopPrice;
        return this;
    }

    public String text() {
        return text;
    }

    public OrderCancelReplaceRequest text(String text) {
        this.text = text;
        return this;
    }

    public TimeInForce tif() {
        return tif;
    }

    public OrderCancelReplaceRequest tif(TimeInForce tif) {
        this.tif = tif;
        return this;
    }

    public String execProviderId() {
        return execProviderId;
    }

    public void execProviderId(String execProviderId) {
        this.execProviderId = execProviderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderCancelReplaceRequest)) return false;
        OrderCancelReplaceRequest<?> that = (OrderCancelReplaceRequest<?>) o;
        return Objects.equal(clOrderId, that.clOrderId) &&
                Objects.equal(orderId, that.orderId) &&
                Objects.equal(origClOrderId, that.origClOrderId) &&
                Objects.equal(instId, that.instId) &&
                Objects.equal(dateTime, that.dateTime) &&
                Objects.equal(limitPrice, that.limitPrice) &&
                Objects.equal(stopPrice, that.stopPrice) &&
                Objects.equal(ordQty, that.ordQty) &&
                Objects.equal(ordType, that.ordType) &&
                Objects.equal(tif, that.tif) &&
                Objects.equal(side, that.side) &&
                Objects.equal(account, that.account) &&
                Objects.equal(text, that.text) &&
                Objects.equal(execProviderId, that.execProviderId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clOrderId, orderId, origClOrderId, instId, dateTime, ordType, limitPrice, stopPrice, ordQty, tif, side, account, text, execProviderId);
    }

    @Override
    public String toString() {
        return "OrderCancelReplaceRequest{" +
                "account='" + account + '\'' +
                ", clOrderId=" + clOrderId +
                ", orderId=" + orderId +
                ", origClOrderId=" + origClOrderId +
                ", instId=" + instId +
                ", dateTime=" + dateTime +
                ", ordType=" + ordType +
                ", limitPrice=" + limitPrice +
                ", stopPrice=" + stopPrice +
                ", ordQty=" + ordQty +
                ", tif=" + tif +
                ", side=" + side +
                ", text='" + text + '\'' +
                ", execProviderId='" + execProviderId + '\'' +
                '}';
    }
}
