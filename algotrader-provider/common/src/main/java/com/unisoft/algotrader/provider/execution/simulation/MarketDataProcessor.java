package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.event.data.MarketData;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.provider.execution.SimulatorConfig;

/**
 * Created by alex on 6/6/15.
 */
public abstract class MarketDataProcessor<T extends MarketData> {

    public abstract double getPrice(Order order, T marketData, SimulatorConfig config);

    public abstract double getQty(Order order, T marketData, SimulatorConfig config);


}
