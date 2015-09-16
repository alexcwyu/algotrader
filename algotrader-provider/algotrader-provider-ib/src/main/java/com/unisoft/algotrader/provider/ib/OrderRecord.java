package com.unisoft.algotrader.provider.ib;

import com.unisoft.algotrader.model.event.execution.Order;

/**
 * Created by alex on 9/17/15.
 */
public class OrderRecord {
    public Order order;
    public double filledQty;
    public double leaveQty;
    public double avgPrice;

    public OrderRecord(Order order){
        this.order = order;
        leaveQty = order.ordQty;
        filledQty = 0;
        avgPrice = 0;
    }

    public void addFill(double lastPrice, double lastQty){
        avgPrice = (avgPrice * filledQty + lastPrice * lastQty) / (lastQty+ filledQty);
        filledQty += lastQty;
        leaveQty -= lastQty;
    }
}
