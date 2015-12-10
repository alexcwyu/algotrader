package com.unisoft.algotrader.trading;

import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.bus.EventBusManager;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.event.execution.OrderCancelReject;
import com.unisoft.algotrader.model.trading.OrdStatus;
import com.unisoft.algotrader.provider.ProviderManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by alex on 5/18/15.
 */
@Singleton
public class BackTestOrderManager extends OrderManager {

    private static final Logger LOG = LogManager.getLogger(BackTestOrderManager.class);

    private final ProviderManager providerManager;
    private final StrategyManager strategyManager;
    private final PortfolioProcessorManager portfolioProcessorManager;

    @Inject
    public BackTestOrderManager(EventBusManager eventBusManager, PortfolioProcessorManager portfolioProcessorManager, ProviderManager providerManager, StrategyManager strategyManager){
        super(eventBusManager);
        this.portfolioProcessorManager = portfolioProcessorManager;
        this.providerManager = providerManager;
        this.strategyManager = strategyManager;
    }

    @Override
    public void onNewOrderRequest(Order order) {
        super.onNewOrderRequest(order);
        providerManager.getExecutionProvider(order.providerId).onNewOrderRequest(order);

        PortfolioProcessor processor = portfolioProcessorManager.getPortfolioProcessor(order.portfolioId);
        if (processor != null) {
            processor.onNewOrderRequest(order);
        }

    }

    @Override
    public void onOrderUpdateRequest(Order order){
        super.onOrderUpdateRequest(order);
        providerManager.getExecutionProvider(order.providerId).onOrderUpdateRequest(order);

        PortfolioProcessor processor = portfolioProcessorManager.getPortfolioProcessor(order.portfolioId);
        if (processor != null) {
            processor.onOrderUpdateRequest(order);
        }
    }

    @Override
    public void onOrderCancelRequest(Order order){
        super.onOrderCancelRequest(order);
        providerManager.getExecutionProvider(order.providerId).onOrderCancelRequest(order);

        PortfolioProcessor processor = portfolioProcessorManager.getPortfolioProcessor(order.portfolioId);
        if (processor != null) {
            processor.onOrderCancelRequest(order);
        }
    }

    @Override
    public void onExecutionReport(ExecutionReport executionReport) {
        LOG.info("onExecutionReport {}", executionReport);

        Order order = processExecutionReport(executionReport);

        if (order != null) {
            OrdStatus prevOrdStatus = order.ordStatus;

            order.add(executionReport);

            orderTable.addOrUpdateOrder(order);

            //notify execution report
            Strategy strategy = strategyManager.get(order.strategyId);
            PortfolioProcessor processor = portfolioProcessorManager.getPortfolioProcessor(order.portfolioId);

            if (strategy != null)
                strategy.onExecutionReport(executionReport);

            if (processor != null) {
                processor.onExecutionReport(executionReport);
            }

            if (prevOrdStatus != order.ordStatus) {
                //TODO orderstatus update
                if (order.ordStatus == OrdStatus.Filled
                        || order.ordStatus == OrdStatus.Cancelled
                        || order.ordStatus == OrdStatus.Rejected) {
                    if (order.filledQty > 0 && order.portfolioId > 0) {
                        //TODO notify portfolio processor
                        if (processor != null) {
                            processor.onNewOrderRequest(order);
                        }
                    }
                }
                strategy.onOrderStatusUpdate(order);
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
            Strategy strategy = strategyManager.get(order.strategyId);
            if (strategy != null)
                strategy.onOrderCancelReject(orderCancelReject);

            PortfolioProcessor processor = portfolioProcessorManager.getPortfolioProcessor(order.portfolioId);
            if (processor != null) {
                processor.onOrderCancelReject(orderCancelReject);
            }
        }
    }

    @Override
    public void onEvent(Event event) {
        event.on(this);
    }

    public void clear(){
        this.orderTable.clear();
    }

}
