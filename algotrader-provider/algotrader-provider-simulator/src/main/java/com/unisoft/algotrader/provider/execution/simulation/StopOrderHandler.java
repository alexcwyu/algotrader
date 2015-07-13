package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.execution.Order;

/**
 * Created by alex on 6/6/15.
 */
public class StopOrderHandler extends AbstractStopLimitOrderHandler {

    public StopOrderHandler(SimulatorConfig config, SimulationExecutor simulationExecutor) {
        super(config, simulationExecutor);
    }

    @Override
    protected boolean stopLimit(Order order, double price, double qty) {
        switch (order.side) {
            case Buy:
            case BuyMinus:
                if (price >= order.stopPrice)
                    return simulationExecutor.execute(order, price, qty);
                break;
            case Sell:
            case SellShort:
                if (price <= order.stopPrice)
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
                if (bar.high >= order.stopPrice)
                    return simulationExecutor.execute(order, order.stopPrice, qty);
                break;
            case Sell:
            case SellShort:
                if (bar.low <= order.stopPrice)
                    return simulationExecutor.execute(order, order.stopPrice, qty);
                break;
            default:
                throw new UnsupportedOperationException("Order side is not supported : " + order.side);
        }

        return false;
    }
}
