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

    protected Map<Long, Order> providerOrderIdMap = Maps.newHashMap();
    protected Map<Long, Order> clOrderIdOrderMap = Maps.newHashMap();
    protected BiMap<Long, Long> clOrderId2providerOrderIdMap  = HashBiMap.create();

    public Optional<Order> getByProviderOrderId(long providerOrderId){
        return Optional.ofNullable(providerOrderIdMap.get(providerOrderId));
    }

    public Optional<Order> getByClOrderId(long clOrderId){
        return Optional.ofNullable(clOrderIdOrderMap.get(clOrderId));
    }

    public void addOrder(Order order){
        providerOrderIdMap.put(order.getProviderOrderId(), order);
        clOrderIdOrderMap.put(order.getClOrderId(), order);
        clOrderId2providerOrderIdMap.put(order.getClOrderId(), order.getProviderOrderId());
    }

    public void removeOrder(Order order){
        providerOrderIdMap.remove(order.getProviderOrderId());
        clOrderIdOrderMap.remove(order.getClOrderId());
        clOrderId2providerOrderIdMap.remove(order.getClOrderId());
    }
}
