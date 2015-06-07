package com.unisoft.algotrader.order;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.core.OrdStatus;
import com.unisoft.algotrader.core.Portfolio;
import com.unisoft.algotrader.core.PortfolioManager;
import com.unisoft.algotrader.event.Event;
import com.unisoft.algotrader.event.EventBusManager;
import com.unisoft.algotrader.event.execution.*;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.strategy.Strategy;
import com.unisoft.algotrader.strategy.StrategyManager;
import com.unisoft.algotrader.threading.AbstractEventProcessor;
import com.unisoft.algotrader.threading.YieldMultiBufferWaitStrategy;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by alex on 5/18/15.
 */
public class OrderManager extends AbstractEventProcessor implements OrderHandler , ExecutionHandler {

    public static final OrderManager INSTANCE;

    static {
        INSTANCE = new OrderManager();
    }

    private AtomicLong ordId = new AtomicLong();


    private Map<String, Map<Long, Order>> orderMap = Maps.newConcurrentMap();

    public OrderManager(){
        super(new YieldMultiBufferWaitStrategy(),  null, EventBusManager.INSTANCE.executionReportRB, EventBusManager.INSTANCE.orderRB, EventBusManager.INSTANCE.orderStatusRB);
    }

    public long nextOrdId(){
        return ordId.getAndIncrement();
    }

    public Map<Long, Order> getOrders(String instId){
        if (!orderMap.containsKey(instId))
            orderMap.putIfAbsent(instId, Maps.newHashMap());
        return orderMap.get(instId);
    }


    public Order getOrder(String instId, long orderId){
        if (orderMap.containsKey(instId))
            return orderMap.get(instId).get(orderId);
        return null;
    }

    public void addOrder(Order order){
        getOrders(order.instId).put(order.orderId, order);

        //TODO persist
    }

    @Override
    public void onOrder(Order order) {
        System.out.println("OrderManager, onOrder=" + order);

        addOrder(order);

        ProviderManager.INSTANCE.getExecutionProvider(order.execProviderId).onOrder(order);
    }

    // TODO new order
    // TODO cancel order
    // TODO replace order

    @Override
    public void onExecutionReport(ExecutionReport executionReport) {

        System.out.println("OrderManager, onExecutionReport=" + executionReport);

        Order order = getOrder(executionReport.instId, executionReport.orderId);
        if (order != null){
            Strategy strategy = StrategyManager.INSTANCE.get(order.strategyId);
            if (strategy != null)
                strategy.onEvent(executionReport);


            order.add(executionReport);
            if (order.ordStatus == OrdStatus.Filled || order.ordStatus == OrdStatus.Cancelled || order.ordStatus == OrdStatus.Rejected){
                if (order.filledQty > 0 && order.portfolioId != null) {
                    Portfolio portfolio = PortfolioManager.INSTANCE.get(order.portfolioId);
                    if (portfolio != null){
                        portfolio.add(order);
                    }
                }
            }
        }
        else{
            throw new RuntimeException("Cannot found order, executionReport="+executionReport);
        }
    }

    @Override
    public void onOrderCancelReject(OrderCancelReject orderCancelReject) {

    }

    @Override
    public void onEvent(Event event) {
        event.on(this);
    }

    public void clear(){
        this.orderMap.clear();
    }
}
