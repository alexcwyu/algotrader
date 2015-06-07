package com.unisoft.algotrader.event.execution;

import com.google.common.base.Objects;
import com.lmax.disruptor.EventFactory;
import com.unisoft.algotrader.event.Event;
import com.unisoft.algotrader.core.OrdStatus;
import com.unisoft.algotrader.core.OrdType;
import com.unisoft.algotrader.core.Side;
import com.unisoft.algotrader.core.TimeInForce;

/**
 * Created by alex on 5/17/15.
 */
public class ExecutionReport <E extends ExecutionReport<? super E>> implements Event<ExecutionHandler, E> {


    public long execId;
    public long orderId;
    public String instId;

    public long transactionTime;


    public OrdType ordType;
    public OrdStatus ordStatus;


    public double limitPrice;
    public double stopPx;
    public double ordQty;

    public double lastQty;
    public double lastPrice;

    public double filledQty;
    public double avgPx;

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
                ", orderId=" + orderId +
                ", instId='" + instId + '\'' +
                ", transactionTime=" + transactionTime +
                ", ordType=" + ordType +
                ", ordStatus=" + ordStatus +
                ", limitPrice=" + limitPrice +
                ", stopPx=" + stopPx +
                ", ordQty=" + ordQty +
                ", lastQty=" + lastQty +
                ", lastPrice=" + lastPrice +
                ", filledQty=" + filledQty +
                ", avgPx=" + avgPx +
                ", tif=" + tif +
                ", side=" + side +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecutionReport)) return false;
        ExecutionReport<?> that = (ExecutionReport<?>) o;
        return Objects.equal(execId, that.execId) &&
                Objects.equal(orderId, that.orderId) &&
                Objects.equal(transactionTime, that.transactionTime) &&
                Objects.equal(limitPrice, that.limitPrice) &&
                Objects.equal(stopPx, that.stopPx) &&
                Objects.equal(ordQty, that.ordQty) &&
                Objects.equal(lastQty, that.lastQty) &&
                Objects.equal(lastPrice, that.lastPrice) &&
                Objects.equal(filledQty, that.filledQty) &&
                Objects.equal(avgPx, that.avgPx) &&
                Objects.equal(instId, that.instId) &&
                Objects.equal(ordType, that.ordType) &&
                Objects.equal(ordStatus, that.ordStatus) &&
                Objects.equal(tif, that.tif) &&
                Objects.equal(side, that.side) &&
                Objects.equal(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(execId, orderId, instId, transactionTime, ordType, ordStatus, limitPrice, stopPx, ordQty, lastQty, lastPrice, filledQty, avgPx, tif, side, text);
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
}
