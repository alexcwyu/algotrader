package com.unisoft.algotrader.model.event.execution;

import com.google.common.base.Objects;
import com.lmax.disruptor.EventFactory;

import java.util.BitSet;

/**
 * Created by alex on 5/24/15.
 */
public class ExecutionEventContainer extends ExecutionEvent<ExecutionEventContainer> {


    public static final int EXEC_REPORT_BIT = 1;
    public static final int ORD_CANCEL_REJ_BIT = 2;
    public static final int ORD_STATUS_UPDATE_BIT = 3;

    public BitSet bitset = new BitSet();

    public ExecutionReport executionReport = null;
    public OrderCancelReject orderCancelReject = null;
    public Order orderStatusUpdate = null;


    public static final EventFactory<ExecutionEventContainer> FACTORY = new EventFactory(){
        @Override
        public ExecutionEventContainer newInstance() {
            return new ExecutionEventContainer();
        }
    };

    @Override
    public void on(ExecutionEventHandler handler) {
        handler.onExecutionEventContainer(this);
    }

    public boolean hasExecutionReport(){
        return bitset.get(EXEC_REPORT_BIT);
    }

    public boolean hasOrderCancelReject(){
        return bitset.get(ORD_CANCEL_REJ_BIT);
    }


    public boolean hasOrderStatusUpdate(){
        return bitset.get(ORD_STATUS_UPDATE_BIT);
    }

    public void setExecutionReport(ExecutionReport executionReport){
        this.executionReport = executionReport;
        bitset.set(EXEC_REPORT_BIT);
    }


    public void setOrderCancelReject(OrderCancelReject orderCancelReject) {
        this.orderCancelReject = orderCancelReject;
        bitset.set(ORD_CANCEL_REJ_BIT);
    }

    public void setOrderStatusUpdate(Order orderStatusUpdate) {
        this.orderStatusUpdate = orderStatusUpdate;
        bitset.set(ORD_STATUS_UPDATE_BIT);
    }


    public void reset(){
        bitset.clear();
        executionReport = null;
        orderCancelReject = null;
        orderStatusUpdate = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecutionEventContainer)) return false;
        ExecutionEventContainer that = (ExecutionEventContainer) o;
        return Objects.equal(bitset, that.bitset) &&
                Objects.equal(executionReport, that.executionReport) &&
                Objects.equal(orderCancelReject, that.orderCancelReject) &&
                Objects.equal(orderStatusUpdate, that.orderStatusUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bitset, executionReport, orderCancelReject, orderStatusUpdate);
    }

    @Override
    public String toString() {
        return "ExecutionEventContainer{" +
                "bitset=" + bitset +
                ", executionReport=" + executionReport +
                ", orderCancelReject=" + orderCancelReject +
                ", orderStatusUpdate=" + orderStatusUpdate +
                "} " + super.toString();
    }
}
