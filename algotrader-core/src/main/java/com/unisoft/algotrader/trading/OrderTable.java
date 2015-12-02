package com.unisoft.algotrader.trading;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.id.ClOrderId;

import java.util.Map;

/**
 * Created by alex on 9/18/15.
 */
public class OrderTable {

    private Map<Long, Order> orderMap = Maps.newHashMap();
    private Map<Long, Map<Long, Order>> instOrderMap = Maps.newHashMap();

    public Map<Long, Order> getOrdersByInstId(long instId){
        if (!instOrderMap.containsKey(instId))
            instOrderMap.putIfAbsent(instId, Maps.newHashMap());
        return instOrderMap.get(instId);
    }


    public Order getOrder(long orderId){
        if (orderMap.containsKey(orderId))
            return orderMap.get(orderId);
        return null;
    }


    public void addOrUpdateOrder(Order order){
        getOrdersByInstId(order.instId).put(order.clOrderId, order);
        orderMap.put(order.clOrderId, order);
    }

    public void removeOrder(Order order) {
        if (getOrdersByInstId(order.instId) != null) {
            getOrdersByInstId(order.instId).remove(order.clOrderId);
        }
        orderMap.remove(order.clOrderId);
    }


    public void clear(){
        this.instOrderMap.clear();
        this.orderMap.clear();
    }

}
