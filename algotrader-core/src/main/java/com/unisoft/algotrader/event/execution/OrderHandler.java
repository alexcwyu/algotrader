package com.unisoft.algotrader.event.execution;

import com.unisoft.algotrader.event.EventHandler;

/**
 * Created by alex on 4/16/15.
 */
public interface OrderHandler extends EventHandler{
    void onOrder(Order order);
}

