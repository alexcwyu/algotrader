package com.unisoft.algotrader.model.event.bus;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.multi.NoWaitStrategy;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.event.data.MarketDepth;
import com.unisoft.algotrader.model.event.execution.ExecutionEventContainer;
import com.unisoft.algotrader.model.event.execution.OrderEventContainer;

import javax.inject.Singleton;

/**
 * Created by alex on 5/17/15.
 */
@Singleton
public abstract class EventBusManager {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 64;
//
//    @Deprecated
//    public final RingBuffer<MarketDataContainer> rawMarketDataRB
//            = RingBuffer.createSingleProducer(MarketDataContainer.FACTORY, DEFAULT_BUFFER_SIZE, new NoWaitStrategy());
//
    protected final RingBuffer<MarketDataContainer> marketDataRB
            = RingBuffer.createSingleProducer(MarketDataContainer.FACTORY, DEFAULT_BUFFER_SIZE, new NoWaitStrategy());
//
    protected final RingBuffer<MarketDepth> marketDepthRB
            = RingBuffer.createSingleProducer(MarketDepth.FACTORY, DEFAULT_BUFFER_SIZE, new NoWaitStrategy());
//
    protected final RingBuffer<ExecutionEventContainer> executionEventRB
            = RingBuffer.createSingleProducer(ExecutionEventContainer.FACTORY, DEFAULT_BUFFER_SIZE, new NoWaitStrategy());
//
    protected final RingBuffer<OrderEventContainer> orderEventRB
            = RingBuffer.createSingleProducer(OrderEventContainer.FACTORY, DEFAULT_BUFFER_SIZE, new NoWaitStrategy());
//
//
//
//    private MarketDataEventBus marketDataEventBus = null;
//
//    private MarketDepthEventBus marketDepthEventBus = null;
//
//    private ExecutionEventBus executionEventBus = null;
//
//    private OrderEventBus orderEventBus = null;
//
//    @Inject
//    public EventBusManager(){
//
//    }

    public RingBuffer<MarketDataContainer> getMarketDataRB(){
        return marketDataRB;
    }

    public RingBuffer<MarketDepth> getMarketDepthRB(){
        return marketDepthRB;
    }

    public RingBuffer<ExecutionEventContainer> getExecutionEventRB(){
        return executionEventRB;
    }

    public RingBuffer<OrderEventContainer> getOrderEventRB(){
        return orderEventRB;
    }

    public abstract MarketDataEventBus getMarketDataEventBus();

    public abstract MarketDepthEventBus getMarketDepthEventBus();

    public abstract ExecutionEventBus getExecutionEventBus();

    public abstract OrderEventBus getOrderEventBus();

}
