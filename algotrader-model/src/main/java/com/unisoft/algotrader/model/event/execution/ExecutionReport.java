package com.unisoft.algotrader.model.event.execution;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Enumerated;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.base.Objects;
import com.unisoft.algotrader.model.trading.*;

/**
 * Created by alex on 5/17/15.
 */

@Table(keyspace = "trading", name = "execution_reports")
public class ExecutionReport <E extends ExecutionReport<? super E>> extends ExecutionEvent<E> {

    @PartitionKey(0)
    @Column(name="provider_id")
    public int providerId;

    @PartitionKey(1)
    @Column(name="exec_id")
    public long execId;

    @Column(name="order_id")
    public long orderId;
//
//    @Column(name="strategy_id")
//    public int strategyId;

    @Column(name="cl_order_id")
    public long clOrderId;

    @Column(name="orig_cl_order_id")
    public long origClOrderId;

    @Column(name="inst_id")
    public long instId;

    @Column(name="transaction_time")
    public long transactionTime;

    @Column(name="ord_type")
    @Enumerated
    public OrdType ordType;

    @Column(name="ord_status")
    @Enumerated
    public OrdStatus ordStatus;

    @Column(name="exec_type")
    @Enumerated
    public ExecType execType;

    @Column(name="limit_price")
    public double limitPrice;

    @Column(name="stop_price")
    public double stopPrice;

    @Column(name="ord_qty")
    public double ordQty;

    @Column(name="last_qty")
    public double lastQty;

    @Column(name="last_price")
    public double lastPrice;

    @Column(name="filled_qty")
    public double filledQty;

    @Column(name="avg_price")
    public double avgPrice;

    @Column(name="tif")
    @Enumerated
    public TimeInForce tif;

    @Column(name="side")
    @Enumerated
    public Side side;

    @Column(name="text")
    public String text;

    @Override
    public String toString() {
        return "ExecutionReport{" +
                "execId=" + execId +
                ", clOrderId=" + clOrderId +
                ", origClOrderId=" + origClOrderId +
                ", orderId=" + orderId +
                ", instId='" + instId + '\'' +
                ", transactionTime=" + transactionTime +
                ", ordType=" + ordType +
                ", ordStatus=" + ordStatus +
                ", execType=" + execType +
                ", limitPrice=" + limitPrice +
                ", stopPrice=" + stopPrice +
                ", ordQty=" + ordQty +
                ", lastQty=" + lastQty +
                ", lastPrice=" + lastPrice +
                ", filledQty=" + filledQty +
                ", avgPrice=" + avgPrice +
                ", tif=" + tif +
                ", side=" + side +
                ", getText='" + text + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecutionReport)) return false;
        ExecutionReport<?> that = (ExecutionReport<?>) o;
        return Objects.equal(execId, that.execId) &&
                Objects.equal(clOrderId, that.clOrderId) &&
                Objects.equal(origClOrderId, that.origClOrderId) &&
                Objects.equal(orderId, that.orderId) &&
                Objects.equal(instId, that.instId) &&
                Objects.equal(transactionTime, that.transactionTime) &&
                Objects.equal(limitPrice, that.limitPrice) &&
                Objects.equal(stopPrice, that.stopPrice) &&
                Objects.equal(ordQty, that.ordQty) &&
                Objects.equal(lastQty, that.lastQty) &&
                Objects.equal(lastPrice, that.lastPrice) &&
                Objects.equal(filledQty, that.filledQty) &&
                Objects.equal(avgPrice, that.avgPrice) &&
                Objects.equal(ordType, that.ordType) &&
                Objects.equal(ordStatus, that.ordStatus) &&
                Objects.equal(execType, that.execType) &&
                Objects.equal(tif, that.tif) &&
                Objects.equal(side, that.side) &&
                Objects.equal(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(execId, clOrderId, origClOrderId, orderId, instId, transactionTime, ordType, ordStatus, execType, limitPrice, stopPrice, ordQty, lastQty, lastPrice, filledQty, avgPrice, tif, side, text);
    }

    @Override
    public void on(ExecutionEventHandler handler) {
        handler.onExecutionReport(this);
    }


    public double avgPrice() {
        return avgPrice;
    }

    public ExecutionReport avgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
        return this;
    }

    public long clOrderId() {
        return clOrderId;
    }

    public ExecutionReport clOrderId(long clOrderId) {
        this.clOrderId = clOrderId;
        return this;
    }

    public long execId() {
        return execId;
    }

    public ExecutionReport execId(long execId) {
        this.execId = execId;
        return this;
    }

    public ExecType execType() {
        return execType;
    }

    public ExecutionReport execType(ExecType execType) {
        this.execType = execType;
        return this;
    }

    public double filledQty() {
        return filledQty;
    }

    public ExecutionReport filledQty(double filledQty) {
        this.filledQty = filledQty;
        return this;
    }

    public long instId() {
        return instId;
    }

    public ExecutionReport instId(long instId) {
        this.instId = instId;
        return this;
    }

    public double lastPrice() {
        return lastPrice;
    }

    public ExecutionReport lastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
        return this;
    }

    public double lastQty() {
        return lastQty;
    }

    public ExecutionReport lastQty(double lastQty) {
        this.lastQty = lastQty;
        return this;
    }

    public double limitPrice() {
        return limitPrice;
    }

    public ExecutionReport limitPrice(double limitPrice) {
        this.limitPrice = limitPrice;
        return this;
    }

    public long orderId() {
        return orderId;
    }

    public ExecutionReport orderId(long orderId) {
        this.orderId = orderId;
        return this;
    }

    public double ordQty() {
        return ordQty;
    }

    public ExecutionReport ordQty(double ordQty) {
        this.ordQty = ordQty;
        return this;
    }

    public OrdStatus ordStatus() {
        return ordStatus;
    }

    public ExecutionReport ordStatus(OrdStatus ordStatus) {
        this.ordStatus = ordStatus;
        return this;
    }

    public OrdType ordType() {
        return ordType;
    }

    public ExecutionReport ordType(OrdType ordType) {
        this.ordType = ordType;
        return this;
    }

    public long origClOrderId() {
        return origClOrderId;
    }

    public ExecutionReport origClOrderId(long origClOrderId) {
        this.origClOrderId = origClOrderId;
        return this;
    }

    public int providerId() {
        return providerId;
    }

    public ExecutionReport providerId(int providerId) {
        this.providerId = providerId;
        return this;
    }

    public Side side() {
        return side;
    }

    public ExecutionReport side(Side side) {
        this.side = side;
        return this;
    }

    public double stopPrice() {
        return stopPrice;
    }

    public ExecutionReport stopPrice(double stopPrice) {
        this.stopPrice = stopPrice;
        return this;
    }

    public String text() {
        return text;
    }

    public ExecutionReport text(String text) {
        this.text = text;
        return this;
    }

    public TimeInForce tif() {
        return tif;
    }

    public ExecutionReport tif(TimeInForce tif) {
        this.tif = tif;
        return this;
    }

    public long transactionTime() {
        return transactionTime;
    }

    public ExecutionReport transactionTime(long transactionTime) {
        this.transactionTime = transactionTime;
        return this;
    }
}
