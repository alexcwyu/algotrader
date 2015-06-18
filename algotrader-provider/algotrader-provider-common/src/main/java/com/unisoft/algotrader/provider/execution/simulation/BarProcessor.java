package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.provider.execution.SimulatorConfig;

/**
 * Created by alex on 6/6/15.
 */
public class BarProcessor extends MarketDataProcessor<Bar> {

    public double getPrice(Order order, Bar bar, SimulatorConfig config) {
        if (bar != null)
            switch (config.fillOnBarMode) {
                case LastBarClose:
                case NextBarClose:
                    return bar.close;
                case NextBarOpen:
                    return bar.open;
            }
        return 0.0;
    }

    public double getQty(Order order, Bar bar, SimulatorConfig config) {
        return order.ordQty;
    }
}
