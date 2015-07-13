package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketData;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.Order;

/**
 * Created by alex on 6/6/15.
 */
public abstract class OrderHandler {

    protected final SimulatorConfig config;
    protected final SimulationExecutor simulationExecutor;

    protected static BarProcessor barProcessor = new BarProcessor();
    protected static TradeProcessor tradeProcessor = new TradeProcessor();
    protected static QuoteProcessor quoteProcessor = new QuoteProcessor();

    protected OrderHandler(SimulatorConfig config, SimulationExecutor simulationExecutor){
        this.config = config;
        this.simulationExecutor = simulationExecutor;
    }

    public boolean process(Order order, MarketData marketData){
        if (marketData instanceof  Quote){
            return process(order, (Quote)marketData);
        }
        else if (marketData instanceof  Trade){
            return process(order, (Trade)marketData);
        }
        else if (marketData instanceof  Bar){
            return process(order, (Bar)marketData);
        }
        return false;
    }



    public abstract boolean process(Order order, Quote quote);

    public abstract boolean process(Order order, Trade trade);

    public abstract boolean process(Order order, Bar bar);

    public abstract boolean process(Order order, double price, double qty);


}
