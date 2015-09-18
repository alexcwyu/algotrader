package com.unisoft.algotrader.trading;

import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.EventBusManager;
import com.unisoft.algotrader.model.event.execution.*;
import com.unisoft.algotrader.model.trading.*;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
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

    private AtomicLong clOrderId = new AtomicLong();
    private OrderTable orderTable = new OrderTable();

    @Inject
    public OrderManager(ProviderManager providerManager, StrategyManager strategyManager, EventBusManager eventBusManager){
        super(new NoWaitStrategy(),  null, eventBusManager.executionReportRB, eventBusManager.orderRB, eventBusManager.orderStatusRB);
        this.providerManager = providerManager;
        this.strategyManager = strategyManager;
        this.eventBusManager = eventBusManager;
    }

    public long nextOrdId(){
        return clOrderId.getAndIncrement();
    }


    private void addOrder(Order order){
        orderTable.addOrUpdateOrder(order);
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
        LOG.info("onOrderReplaceRequest {}" , order);
        order.origClOrderId = order.clOrderId;
        order.clOrderId = nextOrdId();
        providerManager.getExecutionProvider(order.execProviderId).onOrderReplaceRequest(order);
    }

    @Override
    public void onOrderCancelRequest(Order order){
        LOG.info("onOrderCancelRequest {}" , order);

        order.origClOrderId = order.clOrderId;
        order.clOrderId = nextOrdId();

        providerManager.getExecutionProvider(order.execProviderId).onOrderCancelRequest(order);

    }

    @Override
    public void onExecutionReport(ExecutionReport executionReport) {
        LOG.info("onExecutionReport {}", executionReport);

        Order order = null;
        if (executionReport.execType == ExecType.PendingCancel ||
                executionReport.execType == ExecType.Cancelled ||
                executionReport.execType == ExecType.PendingReplace ||
                executionReport.execType == ExecType.Replace) {

            order = orderTable.getOrder(executionReport.origClOrderId);
            if (executionReport.execType == ExecType.Replace) {
                orderTable.removeOrder(order);
                order.clOrderId = executionReport.clOrderId;
                order.ordType = executionReport.ordType;
                order.limitPrice = executionReport.limitPrice;
                order.stopPrice = executionReport.stopPrice;
                order.ordQty = executionReport.ordQty;
                order.tif = executionReport.tif;

                orderTable.addOrUpdateOrder(order);
            }
        } else {
            order = orderTable.getOrder(executionReport.clOrderId);
        }
        if (order != null) {
            OrdStatus prevOrdStatus = order.ordStatus;

            order.add(executionReport);

            //TODO presist order
            orderTable.addOrUpdateOrder(order);

            //notify execution report
            //TODO still need this? should we rely on the msg bus?
            Strategy strategy = strategyManager.get(order.strategyId);
            if (strategy != null)
                strategy.onEvent(executionReport);

            if (prevOrdStatus != order.ordStatus) {

                //TODO orderstatus update

                if (order.ordStatus == OrdStatus.Filled
                        || order.ordStatus == OrdStatus.Cancelled
                        || order.ordStatus == OrdStatus.Rejected) {
                    if (order.filledQty > 0 && order.portfolioId != null) {
                        //TODO notify portfolio processor
//                    Portfolio portfolio = PortfolioManager.INSTANCE.get(order.portfolioId);
//                    if (portfolio != null){
//                        portfolio.add(order);
//                    }
                    }
                }
            }


        } else {
            throw new RuntimeException("Cannot found order, executionReport=" + executionReport);
        }

    }

    @Override
    public void onOrderCancelReject(OrderCancelReject orderCancelReject) {
        Order order = orderTable.getOrder(orderCancelReject.clOrderId);
        OrdStatus prevOrdStatus = order.ordStatus;
        order.add(orderCancelReject);

        if(prevOrdStatus != order.ordStatus){
            //TODO notify order status changed.
        }
    }

    @Override
    public void onEvent(Event event) {
        event.on(this);
    }

    public void clear(){
        this.orderTable.clear();
    }

    public Order newLimitOrder(long instId, String strategyId, String providerId, Side side, double price, double qty, TimeInForce tif){
        Order order = new Order();
        order.clOrderId = nextOrdId();
        order.instId = instId;
        order.strategyId = strategyId;
        order.execProviderId = providerId;
        order.side= side;
        order.ordType = OrdType.Limit;
        order.ordQty=qty;
        order.limitPrice = price;
        order.tif = tif;
        return order;
    }

    public Order newMarketOrder(long instId, String strategyId, String providerId, Side side, double qty, TimeInForce tif){
        Order order = new Order();
        order.clOrderId = nextOrdId();
        order.instId = instId;
        order.strategyId = strategyId;
        order.execProviderId = providerId;
        order.side= side;
        order.ordType = OrdType.Market;
        order.ordQty=qty;
        order.tif = tif;
        return order;
    }
}
