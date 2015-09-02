package com.unisoft.algotrader.model.event.execution;

import com.google.common.base.Objects;
import com.lmax.disruptor.EventFactory;
import com.unisoft.algotrader.model.event.Event;

import java.util.BitSet;

/**
 * Created by alex on 5/24/15.
 */
public class ExecutionEventContainer<E extends ExecutionEventContainer<? super E>> implements Event<ExecutionHandler, E> {


    public static final int EXEC_REPORT_BIT = 1;
    public static final int ORD_CANCEL_REJ_BIT = 2;

    public BitSet bitset = new BitSet();

    public ExecutionReport executionReport = new ExecutionReport();
    public OrderCancelReject orderCancelReject = new OrderCancelReject();

    public static final EventFactory<ExecutionEventContainer> FACTORY = new EventFactory(){
        @Override
        public ExecutionEventContainer newInstance() {
            return new ExecutionEventContainer();
        }
    };

    @Override
    public void on(ExecutionHandler handler) {
        handler.onExecutionEventContainer(this);
    }

    @Override
    public void reset() {
        bitset.clear();
        executionReport.reset();
        orderCancelReject.reset();
    }

    public boolean hasExecutionReport(){
        return bitset.get(EXEC_REPORT_BIT);
    }

    public boolean hasOrderCancelReject(){
        return bitset.get(ORD_CANCEL_REJ_BIT);
    }

    public void setExecutionReport(ExecutionReport executionReport){
        this.executionReport.reset();
        if (executionReport != null){
            this.executionReport.copy(executionReport);
            bitset.set(EXEC_REPORT_BIT);
        }
    }


    public void setOrderCancelReject(OrderCancelReject orderCancelReject){
        this.orderCancelReject.reset();
        if (orderCancelReject != null){
            this.orderCancelReject.copy(orderCancelReject);
            bitset.set(ORD_CANCEL_REJ_BIT);
        }
    }

    @Override
    public void copy(E event) {
        if (event.bitset.get(EXEC_REPORT_BIT))
            setExecutionReport(event.executionReport);
        if (event.bitset.get(ORD_CANCEL_REJ_BIT))
            setOrderCancelReject(event.orderCancelReject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecutionEventContainer)) return false;
        ExecutionEventContainer<?> that = (ExecutionEventContainer<?>) o;
        return Objects.equal(bitset, that.bitset) &&
                Objects.equal(executionReport, that.executionReport) &&
                Objects.equal(orderCancelReject, that.orderCancelReject);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bitset, executionReport, orderCancelReject);
    }

    @Override
    public String toString() {
        return "ExecutionEventContainer{" +
                "bitset=" + bitset +
                ", executionReport=" + executionReport +
                ", orderCancelReject=" + orderCancelReject +
                "} " + super.toString();
    }
}
