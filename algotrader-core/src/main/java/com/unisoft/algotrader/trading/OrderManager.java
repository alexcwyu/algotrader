package com.unisoft.algotrader.trading;

import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.bus.EventBusManager;
import com.unisoft.algotrader.model.event.execution.*;
import com.unisoft.algotrader.model.trading.ExecType;
import com.unisoft.algotrader.model.trading.OrdStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by alex on 5/18/15.
 */
@Singleton
public class OrderManager implements OrderEventHandler, ExecutionEventHandler {

    private static final Logger LOG = LogManager.getLogger(OrderManager.class);

    protected final EventBusManager eventBusManager;

    protected OrderTable orderTable = new OrderTable();

    @Inject
    public OrderManager(EventBusManager eventBusManager){
        this.eventBusManager = eventBusManager;
    }

    protected void addOrUpdateOrder(Order order){
        orderTable.addOrUpdateOrder(order);
        //TODO persist
    }

    @Override
    public void onNewOrderRequest(Order order) {
        LOG.info("onNewOrderRequest {}", order);
        addOrUpdateOrder(order);
    }

    @Override
    public void onOrderUpdateRequest(Order order){
        LOG.info("onOrderUpdateRequest {}", order);
        addOrUpdateOrder(order);
    }

    @Override
    public void onOrderCancelRequest(Order order){
        LOG.info("onOrderCancelRequest {}", order);
    }


    protected Order processExecutionReport(ExecutionReport executionReport){

        Order order = null;
        ExecType execType = executionReport.execType;
        if (execType == ExecType.PendingCancel ||
                execType == ExecType.Cancelled ||
                execType == ExecType.PendingReplace ||
                execType == ExecType.Replace) {

            order = orderTable.getOrder(executionReport.strategyId, executionReport.origClOrderId);
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
            order = orderTable.getOrder(executionReport.strategyId, executionReport.clOrderId);
        }


        return order;
    }

    @Override
    public void onExecutionReport(ExecutionReport executionReport) {
        LOG.info("onExecutionReport {}", executionReport);

        Order order = processExecutionReport(executionReport);

        if (order != null) {
            OrdStatus prevOrdStatus = order.ordStatus;

            order.add(executionReport);

            addOrUpdateOrder(order);

            if (prevOrdStatus != order.ordStatus) {
                eventBusManager.getExecutionEventBus().publishOrderStatusUpdate(order);
            }
        } else {
            throw new RuntimeException("Cannot found order, executionReport=" + executionReport);
        }
    }

    @Override
    public void onOrderCancelReject(OrderCancelReject orderCancelReject) {
        Order order = orderTable.getOrder(orderCancelReject.strategyId, orderCancelReject.clOrderId);
        OrdStatus prevOrdStatus = order.ordStatus;
        order.add(orderCancelReject);

        addOrUpdateOrder(order);
        if(prevOrdStatus != order.ordStatus){
            eventBusManager.getExecutionEventBus().publishOrderStatusUpdate(order);
        }
    }


    @Override
    public void onOrderStatusUpdate(Order orderStatusUpdate){

        Order order = orderTable.getOrder(orderStatusUpdate.strategyId, orderStatusUpdate.clOrderId);
        OrdStatus prevOrdStatus = order.ordStatus;
        order.ordStatus = orderStatusUpdate.ordStatus;

        addOrUpdateOrder(order);
    }



    @Override
    public void onEvent(Event event) {
        event.on(this);
    }

    public void clear(){
        this.orderTable.clear();
    }




}
