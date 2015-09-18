package com.unisoft.algotrader.trading;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.execution.Order;

import java.util.Map;

/**
 * Created by alex on 9/18/15.
 */
public class OrderTable {

    private Map<Long, Order> orderMap = Maps.newConcurrentMap();
    private Map<Long, Map<Long, Order>> instOrderMap = Maps.newConcurrentMap();

    public Map<Long, Order> getOrders(long instId){
        if (!instOrderMap.containsKey(instId))
            instOrderMap.putIfAbsent(instId, Maps.newHashMap());
        return instOrderMap.get(instId);
    }


    public Order getOrder(long clOrderId){
        if (orderMap.containsKey(clOrderId))
            return orderMap.get(clOrderId);
        return null;
    }


    public void addOrUpdateOrder(Order order){
        getOrders(order.instId).put(order.clOrderId, order);
        orderMap.put(order.clOrderId, order);
    }

    public void removeOrder(Order order) {
        if (getOrders(order.instId) != null) {
            getOrders(order.instId).remove(order.clOrderId);
        }
        orderMap.remove(order.clOrderId);
    }


    public void clear(){
        this.instOrderMap.clear();
        this.orderMap.clear();
    }

}
