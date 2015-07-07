package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.provider.execution.SimulationExecutor;
import com.unisoft.algotrader.provider.execution.SimulatorConfig;

/**
 * Created by alex on 6/6/15.
 */
public class LimitOrderHandler extends AbstractStopLimitOrderHandler {

    public LimitOrderHandler(SimulatorConfig config, SimulationExecutor simulationExecutor) {
        super(config, simulationExecutor);
    }

    @Override
    protected boolean stopLimit(Order order, double price, double qty) {
        switch (order.side) {
            case Buy:
            case BuyMinus:
                if (price <= order.limitPrice)
                    return simulationExecutor.execute(order, price, qty);
                break;
            case Sell:
            case SellShort:
                if (price >= order.limitPrice)
                    return simulationExecutor.execute(order, price, qty);
                break;
            default:
                throw new UnsupportedOperationException("Order side is not supported : " + order.side);
        }

        return false;
    }

    @Override
    protected boolean stopLimit(Order order, Bar bar, double qty) {
        switch (order.side) {
            case Buy:
            case BuyMinus:
                if (bar.low <= order.limitPrice)
                    return simulationExecutor.execute(order, order.limitPrice, qty);
                break;
            case Sell:
            case SellShort:
                if (bar.high >= order.limitPrice)
                    return simulationExecutor.execute(order, order.limitPrice, qty);
                break;
            default:
                throw new UnsupportedOperationException("Order side is not supported : " + order.side);
        }

        return false;
    }
}
