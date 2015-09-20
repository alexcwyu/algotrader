package com.unisoft.algotrader.model.event.execution;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.base.Objects;
import com.lmax.disruptor.EventFactory;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.trading.*;

/**
 * Created by alex on 5/17/15.
 */

@Table(keyspace = "trading", name = "execution_reports")
public class ExecutionReport <E extends ExecutionReport<? super E>> implements Event<ExecutionHandler, E> {

    @PartitionKey
    @Column(name="exec_id")
    public long execId;

    @Column(name="exec_provider_id")
    public String execProviderId;

    @Column(name="cl_order_id")
    public long clOrderId;

    @Column(name="order_id")
    public long orderId;

    @Column(name="orig_cl_order_id")
    public long origClOrderId;

    @Column(name="inst_id")
    public long instId;

    @Column(name="transaction_time")
    public long transactionTime;

    @Column(name="ord_type")
    public OrdType ordType;

    @Column(name="ord_status")
    public OrdStatus ordStatus;

    @Column(name="exec_type")
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

    public long getClOrderId() {
        return clOrderId;
    }

    public long getOrigOrderId() {
        return origClOrderId;
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

    public void setClOrderId(long clOrderId) {
        this.clOrderId = clOrderId;
    }

    public void setOrigClOrderId(long origClOrderId) {
        this.origClOrderId = origClOrderId;
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

    public void setExecProviderId(String execProviderId){
        this.execProviderId = execProviderId;
    }

    public String getExecProviderId(){
        return execProviderId;
    }
}
