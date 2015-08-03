package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.execution.Order;

/**
 * Created by alex on 6/6/15.
 */
public class TrailingStopOrderHandler extends AbstractStopLimitOrderHandler {

    public TrailingStopOrderHandler(SimulatorConfig config, SimulationExecutor simulationExecutor) {
        super(config, simulationExecutor);
    }

    private void initOrderTrailingStopExecPrice(Order order) {
        if (order.trailingStopExecPrice == 0) {
            switch (order.side) {
                case Buy:
                //case BuyMinus:
                    order.trailingStopExecPrice = Double.MAX_VALUE;
                    break;
                case Sell:
                case SellShort:
                    order.trailingStopExecPrice = Double.MIN_VALUE;
                    break;
                default:
                    throw new UnsupportedOperationException("Order side is not supported : " + order.side);
            }
        }
    }

    @Override
    protected boolean stopLimit(Order order, double price, double qty) {
        initOrderTrailingStopExecPrice(order);
        switch (order.side) {
            case Buy:
            //case BuyMinus:
                order.trailingStopExecPrice = Math.min(order.trailingStopExecPrice, price + order.stopPrice);

                if (price >= order.trailingStopExecPrice) {
                    return simulationExecutor.execute(order, price, qty);
                }
                break;
            case Sell:
            case SellShort:
                order.trailingStopExecPrice = Math.max(order.trailingStopExecPrice, price - order.stopPrice);

                if (price <= order.trailingStopExecPrice) {
                    return simulationExecutor.execute(order, price, qty);
                }
                break;
            default:
                throw new UnsupportedOperationException("Order side is not supported : " + order.side);
        }

        return false;
    }

    @Override
    protected boolean stopLimit(Order order, Bar bar, double qty) {
        initOrderTrailingStopExecPrice(order);
        switch (order.side) {
            case Buy:
            //case BuyMinus:
                order.trailingStopExecPrice = Math.min(order.trailingStopExecPrice, bar.low + order.stopPrice);

                if (bar.high >= order.trailingStopExecPrice) {
                    return simulationExecutor.execute(order, order.trailingStopExecPrice, qty);
                }
                break;
            case Sell:
            case SellShort:
                order.trailingStopExecPrice = Math.max(order.trailingStopExecPrice, bar.high - order.stopPrice);

                if (bar.low <= order.trailingStopExecPrice) {
                    return simulationExecutor.execute(order, order.trailingStopExecPrice, qty);
                }
                break;
            default:
                throw new UnsupportedOperationException("Order side is not supported : " + order.side);
        }

        return false;
    }
}
