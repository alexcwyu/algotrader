package com.unisoft.algotrader.trading;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.id.ClOrderId;

import java.util.Map;

/**
 * Created by alex on 9/18/15.
 */
public class OrderTable {

    private Map<ClOrderId, Order> orderMap = Maps.newHashMap();
    private Map<Long, Map<ClOrderId, Order>> instOrderMap = Maps.newHashMap();

    public Map<ClOrderId, Order> getOrdersByInstId(long instId){
        if (!instOrderMap.containsKey(instId))
            instOrderMap.putIfAbsent(instId, Maps.newHashMap());
        return instOrderMap.get(instId);
    }


    public Order getOrder(int strategyId, long OrderId){
        ClOrderId clOrderId = new ClOrderId(strategyId, OrderId);
        if (orderMap.containsKey(clOrderId))
            return orderMap.get(clOrderId);
        return null;
    }


    public void addOrUpdateOrder(Order order){
        ClOrderId clOrderId = new ClOrderId(order.strategyId, order.clOrderId);
        getOrdersByInstId(order.instId).put(clOrderId, order);
        orderMap.put(clOrderId, order);
    }

    public void removeOrder(Order order) {
        ClOrderId clOrderId = new ClOrderId(order.strategyId, order.clOrderId);
        if (getOrdersByInstId(order.instId) != null) {
            getOrdersByInstId(order.instId).remove(clOrderId);
        }
        orderMap.remove(clOrderId);
    }


    public void clear(){
        this.instOrderMap.clear();
        this.orderMap.clear();
    }

}
