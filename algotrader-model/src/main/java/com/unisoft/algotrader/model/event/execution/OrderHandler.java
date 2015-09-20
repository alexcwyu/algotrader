package com.unisoft.algotrader.model.event.execution;

import com.unisoft.algotrader.model.event.EventHandler;

/**
 * Created by alex on 4/16/15.
 */
public interface OrderHandler extends EventHandler {
//    void onNewOrderRequest(Order order);
//    void onOrderReplaceRequest(OrderCancelReplaceRequest orderCancelReplaceRequest);
//    void onOrderCancelRequest(OrderCancelRequest orderCancelRequest);


    void onNewOrderRequest(Order order);
    void onOrderUpdateRequest(Order order);
    void onOrderCancelRequest(Order order);
}

