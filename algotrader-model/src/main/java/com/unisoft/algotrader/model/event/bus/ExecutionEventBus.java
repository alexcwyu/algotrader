package com.unisoft.algotrader.model.event.bus;

import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.event.execution.OrderCancelReject;

/**
 * Created by alex on 9/20/15.
 */ //TODO
public interface ExecutionEventBus {
    void publishExecutionReport(ExecutionReport executionReport);

    void publishOrderStatusUpdate(Order order);

    void publishOrderCancelReject(OrderCancelReject orderCancelReject);
}
