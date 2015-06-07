package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.provider.execution.SimulationExecutor;
import com.unisoft.algotrader.provider.execution.SimulatorConfig;

/**
 * Created by alex on 6/6/15.
 */
public class StopLimitOrderHandler extends AbstractStopLimitOrderHandler {


    public StopLimitOrderHandler(SimulatorConfig config, SimulationExecutor simulationExecutor) {
        super(config, simulationExecutor);
    }

    @Override
    protected boolean stopLimit(Order order, double price, double qty) {
        switch (order.side) {
            case Buy:
            case BuyMinus:
                if (!order.stopLimitReady) {
                    if (price >= order.stopPx) {
                        order.stopLimitReady = true;
                    }
                } else {
                    if (price <= order.limitPrice) {
                        return simulationExecutor.execute(order, price, qty);
                    }
                }
                break;
            case Sell:
            case SellShort:

                if (!order.stopLimitReady) {
                    if (price <= order.stopPx) {
                        order.stopLimitReady = true;
                    }
                } else {
                    if (price >= order.limitPrice) {
                        return simulationExecutor.execute(order, price, qty);
                    }
                }
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
                if (!order.stopLimitReady) {
                    if (bar.high >= order.stopPx) {
                        order.stopLimitReady = true;
                    }
                } else {
                    if (bar.low <= order.limitPrice) {
                        return simulationExecutor.execute(order, order.limitPrice, qty);
                    }
                }
                break;
            case Sell:
            case SellShort:

                if (!order.stopLimitReady) {
                    if (bar.low <= order.stopPx) {
                        order.stopLimitReady = true;
                    }
                } else {
                    if (bar.high >= order.limitPrice) {
                        return simulationExecutor.execute(order, order.limitPrice, qty);
                    }
                }
                break;
            default:
                throw new UnsupportedOperationException("Order side is not supported : " + order.side);
        }

        return false;
    }
}
