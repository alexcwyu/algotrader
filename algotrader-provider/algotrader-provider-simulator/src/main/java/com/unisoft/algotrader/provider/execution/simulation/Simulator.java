package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataHandler;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.trading.Strategy;

;

/**
 * Created by alex on 6/2/15.
 */
public class Simulator implements MarketDataHandler {

    private final Strategy[] strategies;

    private final SimulationExecutor simulationExecutor;

    public Simulator(SimulationExecutor simulationExecutor, Strategy ... strategies){
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
