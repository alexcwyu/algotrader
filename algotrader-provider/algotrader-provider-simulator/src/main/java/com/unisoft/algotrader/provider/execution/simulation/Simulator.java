package com.unisoft.algotrader.provider.execution.simulation;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.data.*;
import com.unisoft.algotrader.trading.Strategy;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;
import com.unisoft.algotrader.utils.threading.disruptor.waitstrategy.NoWaitStrategy;

;

/**
 * Created by alex on 6/2/15.
 */
public class Simulator extends MultiEventProcessor implements MarketDataHandler {

    private final Strategy[] strategies;

    private final SimulationExecutor simulationExecutor;

    public Simulator(SimulationExecutor simulationExecutor, RingBuffer<MarketDataContainer> marketDataRB, Strategy ... strategies){
        super(new NoWaitStrategy(),  null, marketDataRB);
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
