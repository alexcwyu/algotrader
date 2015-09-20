package com.unisoft.algotrader.model.event.execution;

import com.unisoft.algotrader.model.event.EventHandler;

/**
 * Created by alex on 4/16/15.
 */
public interface OrderEventHandler extends EventHandler {

    default void onNewOrderRequest(Order order){}
    default void onOrderUpdateRequest(Order order){}
    default void onOrderCancelRequest(Order order){}


    default void onOrderEventContainer(OrderEventContainer orderEventContainer){
        if (orderEventContainer.hasNewOrderRequest()){
            onNewOrderRequest(orderEventContainer.newOrderRequest);
        }
        if (orderEventContainer.hasReplaceOrderRequest()){
            onOrderUpdateRequest(orderEventContainer.replaceOrderRequest);
        }
        if (orderEventContainer.hasCancelOrderRequest()){
            onOrderCancelRequest(orderEventContainer.cancelOrderRequest);
        }
    }
}

