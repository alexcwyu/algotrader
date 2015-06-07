package com.unisoft.algotrader.provider.execution;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.event.Event;
import com.unisoft.algotrader.event.data.*;
import com.unisoft.algotrader.strategy.Strategy;
import com.unisoft.algotrader.threading.AbstractEventProcessor;
import com.unisoft.algotrader.threading.YieldMultiBufferWaitStrategy;

/**
 * Created by alex on 6/2/15.
 */
public class Simulator extends AbstractEventProcessor implements MarketDataHandler {

    private final Strategy[] strategies;

    private final SimulationExecutor simulationExecutor;

    public Simulator(SimulationExecutor simulationExecutor, RingBuffer<MarketDataContainer> marketDataRB, Strategy ... strategies){
        super(new YieldMultiBufferWaitStrategy(),  null, marketDataRB);
        this.simulationExecutor = simulationExecutor;
        this.strategies = strategies;
    }


    @Override
    public void onEvent(Event event) {
        event.on(this);
    }


    @Override
    public void onBar(Bar bar) {
        simulationExecutor.onEvent(bar);
        for (int i = 0; i < strategies.length; i++) {
            strategies[i].onEvent(bar);
        }
    }

    @Override
    public void onQuote(Quote quote) {
        simulationExecutor.onEvent(quote);
        for (int i = 0; i < strategies.length; i++) {
            strategies[i].onEvent(quote);
        }
    }

    @Override
    public void onTrade(Trade trade) {
        simulationExecutor.onEvent(trade);
        for (int i = 0; i < strategies.length; i++) {
            strategies[i].onEvent(trade);
        }
    }
}
