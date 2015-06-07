package com.unisoft.algotrader.event.execution;

import com.lmax.disruptor.EventFactory;
import com.unisoft.algotrader.event.Event;

/**
 * Created by alex on 5/24/15.
 */
public class OrderCancelReject<E extends OrderCancelReject<? super E>> implements Event<ExecutionHandler, E> {
    //TODO



    public static final EventFactory<OrderCancelReject> FACTORY = new EventFactory(){
    @Override
    public OrderCancelReject newInstance() {
        return new OrderCancelReject();
        }
    };

    @Override
    public void on(ExecutionHandler handler) {
        handler.onOrderCancelReject(this);
    }

    @Override
    public void reset() {

    }

    @Override
    public void copy(E event) {

    }
}
