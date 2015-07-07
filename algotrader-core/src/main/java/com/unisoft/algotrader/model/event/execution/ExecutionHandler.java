package com.unisoft.algotrader.model.event.execution;

import com.unisoft.algotrader.model.event.EventHandler;

/**
 * Created by alex on 4/16/15.
 */
public interface ExecutionHandler extends EventHandler {

    default void onExecutionReport(ExecutionReport executionReport){

    }

    default void onOrderCancelReject(OrderCancelReject orderCancelReject){

    }

    default void onExecutionEventContainer(ExecutionEventContainer executionEventContainer){
        if (executionEventContainer.hasExecutionReport()){
            onExecutionReport(executionEventContainer.executionReport);
        }
        if (executionEventContainer.hasOrderCancelReject()){
            onOrderCancelReject(executionEventContainer.orderCancelReject);
        }
    }

}

