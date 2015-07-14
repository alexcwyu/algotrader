package com.unisoft.algotrader.model.event.execution;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.base.Objects;
import com.lmax.disruptor.EventFactory;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.trading.OrdStatus;
import com.unisoft.algotrader.model.trading.OrdType;
import com.unisoft.algotrader.model.trading.Side;
import com.unisoft.algotrader.model.trading.TimeInForce;

/**
 * Created by alex on 5/17/15.
 */

@Table(keyspace = "trading", name = "execution_reports")
public class ExecutionReport <E extends ExecutionReport<? super E>> implements Event<ExecutionHandler, E> {

    @PartitionKey
    @Column(name="exec_id")
    public long execId;
    @Column(name="order_id")
    public long orderId;
    @Column(name="inst_id")
    public long instId;

    @Column(name="transaction_time")
    public long transactionTime;

    @Column(name="ord_type")
    public OrdType ordType;

    @Column(name="ord_status")
    public OrdStatus ordStatus;

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

    public TimeInForce tif;

    public Side side;

    public String text;

    public static final EventFactory<ExecutionReport> FACTORY = new EventFactory(){
        @Override
        public ExecutionReport newInstance() {
            return new ExecutionReport();
        }
    };

    @Override
    public String toString() {
        return "ExecutionReport{" +
                "execId=" + execId +
                ", getOrderId=" + orderId +
                ", instId='" + instId + '\'' +
                ", transactionTime=" + transactionTime +
                ", ordType=" + ordType +
                ", ordStatus=" + ordStatus +
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecutionReport)) return false;
        ExecutionReport<?> that = (ExecutionReport<?>) o;
        return Objects.equal(execId, that.execId) &&
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
                Objects.equal(tif, that.tif) &&
                Objects.equal(side, that.side) &&
                Objects.equal(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(execId, orderId, instId, transactionTime, ordType, ordStatus, limitPrice, stopPrice, ordQty, lastQty, lastPrice, filledQty, avgPrice, tif, side, text);
    }

    @Override
    public void on(ExecutionHandler handler) {
        handler.onExecutionReport(this);
    }

    @Override
    public void reset() {

    }

    @Override
    public void copy(E event) {

    }


    public long getExecId() {
        return execId;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getInstId() {
        return instId;
    }

    public long getTransactionTime() {
        return transactionTime;
    }

    public OrdType getOrdType() {
        return ordType;
    }

    public OrdStatus getOrdStatus() {
        return ordStatus;
    }

    public double getLimitPrice() {
        return limitPrice;
    }

    public double getStopPrice() {
        return stopPrice;
    }

    public double getOrdQty() {
        return ordQty;
    }

    public double getLastQty() {
        return lastQty;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public double getFilledQty() {
        return filledQty;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public TimeInForce getTif() {
        return tif;
    }

    public Side getSide() {
        return side;
    }

    public String getText() {
        return text;
    }

    public void setExecId(long execId) {
        this.execId = execId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setInstId(long instId) {
        this.instId = instId;
    }

    public void setTransactionTime(long transactionTime) {
        this.transactionTime = transactionTime;
    }

    public void setOrdType(OrdType ordType) {
        this.ordType = ordType;
    }

    public void setOrdStatus(OrdStatus ordStatus) {
        this.ordStatus = ordStatus;
    }

    public void setLimitPrice(double limitPrice) {
        this.limitPrice = limitPrice;
    }

    public void setStopPrice(double stopPrice) {
        this.stopPrice = stopPrice;
    }

    public void setOrdQty(double ordQty) {
        this.ordQty = ordQty;
    }

    public void setLastQty(double lastQty) {
        this.lastQty = lastQty;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public void setFilledQty(double filledQty) {
        this.filledQty = filledQty;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public void setTif(TimeInForce tif) {
        this.tif = tif;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public void setText(String text) {
        this.text = text;
    }
}
