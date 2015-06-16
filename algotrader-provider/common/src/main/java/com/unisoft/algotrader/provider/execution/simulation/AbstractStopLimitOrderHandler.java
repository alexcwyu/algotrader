package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.provider.execution.SimulationExecutor;
import com.unisoft.algotrader.provider.execution.SimulatorConfig;

/**
 * Created by alex on 6/6/15.
 */
public abstract class AbstractStopLimitOrderHandler extends OrderHandler {

    public AbstractStopLimitOrderHandler(SimulatorConfig config, SimulationExecutor simulationExecutor) {
        super(config, simulationExecutor);
    }

    @Override
    public boolean process(Order order, Quote quote) {
        if (quote != null) {
            double price = quoteProcessor.getPrice(order, quote, config);
            double qty = quoteProcessor.getQty(order, quote, config);
            return stopLimit(order, price, qty);
        }
        return false;
    }

    @Override
    public boolean process(Order order, Trade trade) {
        if (trade != null) {
            double price = tradeProcessor.getPrice(order, trade, config);
            double qty = tradeProcessor.getQty(order, trade, config);
            return stopLimit(order, price, qty);
        }
        return false;
    }

    @Override
    public boolean process(Order order, Bar bar) {
        if (bar != null) {
            //double price = barProcessor.getPrice(order, bar, config);
            double qty = barProcessor.getQty(order, bar, config);
            return stopLimit(order, bar, qty);
        }
        return false;
    }

    @Override
    public boolean process(Order order, double price, double qty){
        return stopLimit(order, price, qty);
    }


    protected abstract boolean stopLimit(Order order, double price, double qty);

    protected abstract boolean stopLimit(Order order, Bar bar, double qty);
}
