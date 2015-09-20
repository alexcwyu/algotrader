package com.unisoft.algotrader.event.bus;

import com.unisoft.algotrader.model.event.bus.*;

import javax.inject.Inject;

/**
 * Created by alex on 9/20/15.
 */
public class DefaultEventBusManager extends EventBusManager {

    public final MarketDataEventBus marketDataEventBus;
    public final MarketDepthEventBus marketDepthEventBus;
    public final ExecutionEventBus executionEventBus;
    public final OrderEventBus orderEventBus;

    @Inject
    public DefaultEventBusManager(){
        marketDataEventBus = new RingBufferMarketDataEventBus(marketDataRB);
        marketDepthEventBus = new RingBufferMarketDepthEventBus(marketDepthRB);
        executionEventBus = new RingBufferExecutionEventBus(executionEventRB);
        orderEventBus = new RingBufferOrderEventBus(orderEventRB);
    }

    public MarketDataEventBus getMarketDataEventBus(){
        return marketDataEventBus;
    }

    public MarketDepthEventBus getMarketDepthEventBus(){
        return marketDepthEventBus;
    }

    public ExecutionEventBus getExecutionEventBus(){
        return executionEventBus;
    }

    public OrderEventBus getOrderEventBus(){
        return orderEventBus;
    }

}
