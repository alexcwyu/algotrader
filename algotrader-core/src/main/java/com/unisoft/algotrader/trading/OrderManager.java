package com.unisoft.algotrader.trading;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.EventBusManager;
import com.unisoft.algotrader.model.event.execution.*;
import com.unisoft.algotrader.model.trading.OrdStatus;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by alex on 5/18/15.
 */
@Singleton
public class OrderManager extends MultiEventProcessor implements OrderHandler, ExecutionHandler {

    private static final Logger LOG = LogManager.getLogger(OrderManager.class);

    private final ProviderManager providerManager;
    private final StrategyManager strategyManager;
    private final EventBusManager eventBusManager;

    private AtomicLong ordId = new AtomicLong();
    private Map<Long, Map<Long, Order>> orderMap = Maps.newConcurrentMap();

    @Inject
    public OrderManager(ProviderManager providerManager, StrategyManager strategyManager, EventBusManager eventBusManager){
        super(new NoWaitStrategy(),  null, eventBusManager.executionReportRB, eventBusManager.orderRB, eventBusManager.orderStatusRB);
        this.providerManager = providerManager;
        this.strategyManager = strategyManager;
        this.eventBusManager = eventBusManager;
    }

    public long nextOrdId(){
        return ordId.getAndIncrement();
    }

    public Map<Long, Order> getOrders(long instId){
        if (!orderMap.containsKey(instId))
            orderMap.putIfAbsent(instId, Maps.newHashMap());
        return orderMap.get(instId);
    }


    public Order getOrder(long instId, long orderId){
        if (orderMap.containsKey(instId))
            return orderMap.get(instId).get(orderId);
        return null;
    }

    private void addOrder(Order order){
        getOrders(order.instId).put(order.orderId, order);

        //TODO persist
    }

    @Override
    public void onNewOrderRequest(Order order) {
        LOG.info("onNewOrderRequest {}" , order);

        addOrder(order);
        providerManager.getExecutionProvider(order.execProviderId).onNewOrderRequest(order);
    }

    @Override
    public void onOrderReplaceRequest(Order order){

    }

    @Override
    public void onOrderCancelRequest(Order order){

    }

    @Override
    public void onExecutionReport(ExecutionReport executionReport) {
        LOG.info("onExecutionReport {}", executionReport);

        Order order = getOrder(executionReport.instId, executionReport.orderId);
        if (order != null){
            Strategy strategy = strategyManager.get(order.strategyId);
            if (strategy != null)
                strategy.onEvent(executionReport);


            order.add(executionReport);
            if (order.ordStatus == OrdStatus.Filled || order.ordStatus == OrdStatus.Cancelled || order.ordStatus == OrdStatus.Rejected){
                if (order.filledQty > 0 && order.portfolioId != null) {
                    //TODO fix this
//                    Portfolio portfolio = PortfolioManager.INSTANCE.get(order.portfolioId);
//                    if (portfolio != null){
//                        portfolio.add(order);
//                    }
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
