package com.unisoft.algotrader.event.bus;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.bus.ExecutionEventBus;
import com.unisoft.algotrader.model.event.execution.ExecutionEventContainer;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.event.execution.OrderCancelReject;

/**
 * Created by alex on 9/20/15.
 */
public class RingBufferExecutionEventBus implements ExecutionEventBus {
    private final RingBuffer<ExecutionEventContainer> executionEventRB;

    public RingBufferExecutionEventBus(RingBuffer<ExecutionEventContainer> executionEventRB){
        this.executionEventRB = executionEventRB;
    }

    private long getNextSeq(){
        long seq = executionEventRB.next();
        return seq;
    }

    private ExecutionEventContainer getNextEventContainer(long seq){
        ExecutionEventContainer container = executionEventRB.get(seq);
        container.reset();
        return container;
    }

    @Override
    public void publishExecutionReport(ExecutionReport executionReport) {
        long seq = getNextSeq();
        getNextEventContainer(seq).setExecutionReport(executionReport);
        executionEventRB.publish(seq);
    }

    @Override
    public void publishOrderStatusUpdate(Order order) {
        long seq = getNextSeq();
        getNextEventContainer(seq).setOrderStatusUpdate(order);
        executionEventRB.publish(seq);
    }

    @Override
    public void publishOrderCancelReject(OrderCancelReject orderCancelReject) {
        long seq = getNextSeq();
        getNextEventContainer(seq).setOrderCancelReject(orderCancelReject);
        executionEventRB.publish(seq);
    }
}
