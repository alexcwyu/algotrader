package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.provider.execution.SimulatorConfig;

/**
 * Created by alex on 6/6/15.
 */
public class TradeProcessor extends MarketDataProcessor<Trade> {

    public double getPrice(Order order, Trade trade, SimulatorConfig config) {
        if (trade != null && trade.price > 0) {
            return trade.price;
        }
        return 0.0;
    }

    public double getQty(Order order, Trade trade, SimulatorConfig config) {
        return order.ordQty;
    }
}
