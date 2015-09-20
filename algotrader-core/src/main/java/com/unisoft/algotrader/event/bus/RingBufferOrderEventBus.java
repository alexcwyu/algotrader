package com.unisoft.algotrader.event.bus;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.bus.OrderEventBus;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.event.execution.OrderEventContainer;

/**
 * Created by alex on 9/20/15.
 */
public class RingBufferOrderEventBus implements OrderEventBus{

    private final RingBuffer<OrderEventContainer> orderEventRB;

    public RingBufferOrderEventBus(RingBuffer<OrderEventContainer> orderEventRB){
        this.orderEventRB = orderEventRB;
    }

    private long getNextSeq(){
        long seq = orderEventRB.next();
        return seq;
    }

    private OrderEventContainer getNextEventContainer(long seq){
        OrderEventContainer container = orderEventRB.get(seq);
        container.reset();
        return container;
    }

    @Override
    public void cancelOrder(Order order) {
        long seq = getNextSeq();
        getNextEventContainer(seq).setCancelOrderRequest(order);
        orderEventRB.publish(seq);
    }

    @Override
    public void placeOrder(Order order) {
        long seq = getNextSeq();
        getNextEventContainer(seq).setNewOrderRequest(order);
        orderEventRB.publish(seq);
    }

    @Override
    public void replaceOrder(Order order) {
        long seq = getNextSeq();
        getNextEventContainer(seq).setReplaceOrderRequest(order);
        orderEventRB.publish(seq);
    }
}
