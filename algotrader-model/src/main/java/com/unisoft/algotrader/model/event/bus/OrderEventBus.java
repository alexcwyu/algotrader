package com.unisoft.algotrader.model.event.bus;

import com.unisoft.algotrader.model.event.execution.Order;

/**
 * Created by alex on 9/20/15.
 */
public interface OrderEventBus {
    void placeOrder(Order order);

    void cancelOrder(Order order);

    void replaceOrder(Order order);
}
