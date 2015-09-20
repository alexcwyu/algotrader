package com.unisoft.algotrader.provider.ib;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.execution.Order;

import java.util.Map;
import java.util.Optional;

/**
 * Created by alex on 9/16/15.
 */
public class OrderRegistry {

    protected Map<Long, Order> orderIdMap = Maps.newHashMap();
    protected Map<Long, Order> clOrderIdOrderMap = Maps.newHashMap();
    protected BiMap<Long, Long> clOrderId2OrderIdMap  = HashBiMap.create();

    public Optional<Order> getByOrderId(long orderId){
        return Optional.ofNullable(orderIdMap.get(orderId));
    }

    public Optional<Order> getByClOrderId(long clOrderId){
        return Optional.ofNullable(clOrderIdOrderMap.get(clOrderId));
    }

    public void addOrder(Order order){
        orderIdMap.put(order.orderId(), order);
        clOrderIdOrderMap.put(order.clOrderId(), order);
        clOrderId2OrderIdMap.put(order.clOrderId(), order.orderId());
    }

    public void removeOrder(Order order){
        orderIdMap.remove(order.orderId());
        clOrderIdOrderMap.remove(order.clOrderId());
        clOrderId2OrderIdMap.remove(order.clOrderId());
    }
}
