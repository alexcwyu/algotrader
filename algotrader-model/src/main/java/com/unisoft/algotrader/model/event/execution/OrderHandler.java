package com.unisoft.algotrader.model.event.execution;

import com.unisoft.algotrader.model.event.EventHandler;

/**
 * Created by alex on 4/16/15.
 */
public interface OrderHandler extends EventHandler {
    void onNewOrderRequest(Order order);
    void onOrderReplaceRequest(Order order);
    void onOrderCancelRequest(Order order);
}

