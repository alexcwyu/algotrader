package com.unisoft.algotrader.model.event.execution;

import com.unisoft.algotrader.model.event.EventHandler;

/**
 * Created by alex on 4/16/15.
 */
public interface ExecutionEventHandler extends EventHandler {

    default void onExecutionReport(ExecutionReport executionReport){

    }

    default void onOrderCancelReject(OrderCancelReject orderCancelReject){

    }

    default void onOrderStatusUpdate(Order orderStatusUpdate){

    }

    default void onExecutionEventContainer(ExecutionEventContainer executionEventContainer){
        if (executionEventContainer.hasExecutionReport()){
            onExecutionReport(executionEventContainer.executionReport);
        }
        if (executionEventContainer.hasOrderCancelReject()){
            onOrderCancelReject(executionEventContainer.orderCancelReject);
        }
        if (executionEventContainer.hasOrderStatusUpdate()){
            onOrderStatusUpdate(executionEventContainer.orderStatusUpdate);
        }
    }

}

