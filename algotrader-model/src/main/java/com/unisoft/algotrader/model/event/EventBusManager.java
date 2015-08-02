package com.unisoft.algotrader.model.event;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;

import static com.unisoft.algotrader.model.event.EventBus.*;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by alex on 5/17/15.
 */
@Singleton
public class EventBusManager {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 64;

    public final RingBuffer<MarketDataContainer> rawMarketDataRB
            = RingBuffer.createSingleProducer(MarketDataContainer.FACTORY, DEFAULT_BUFFER_SIZE, new NoWaitStrategy());

    public final RingBuffer<MarketDataContainer> marketDataRB
            = RingBuffer.createSingleProducer(MarketDataContainer.FACTORY, DEFAULT_BUFFER_SIZE, new NoWaitStrategy());

    public final RingBuffer<Order> orderRB
            = RingBuffer.createSingleProducer(Order.FACTORY, DEFAULT_BUFFER_SIZE, new NoWaitStrategy());

    public final RingBuffer<Order> orderStatusRB
            = RingBuffer.createSingleProducer(Order.FACTORY, DEFAULT_BUFFER_SIZE, new NoWaitStrategy());

    public final RingBuffer<ExecutionReport> executionReportRB
            = RingBuffer.createSingleProducer(ExecutionReport.FACTORY, DEFAULT_BUFFER_SIZE, new NoWaitStrategy());


    private MarketDataEventBus marketDataEventBus = null;

    private ExecutionEventBus executionEventBus = null;

    private GeneralEventBus generalEventBus = null;
    @Inject
    public EventBusManager(){

    }

    public MarketDataEventBus getMarketDataEventBus(){
        return marketDataEventBus;
    }

    public ExecutionEventBus getExecutionEventBus(){
        return executionEventBus;
    }

    public GeneralEventBus getGeneralEventBus(){
        return generalEventBus;
    }
}
