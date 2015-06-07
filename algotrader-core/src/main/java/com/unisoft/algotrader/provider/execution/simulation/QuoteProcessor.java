package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.execution.Order;
import com.unisoft.algotrader.provider.execution.SimulatorConfig;

/**
 * Created by alex on 6/6/15.
 */
public class QuoteProcessor extends MarketDataProcessor<Quote> {

    public double getPrice(Order order, Quote quote, SimulatorConfig config) {
        if (quote != null)
            switch (order.side) {
                case Buy:
                case BuyMinus:
                    if (quote.ask > 0)
                        return quote.ask;
                    break;
                case Sell:
                case SellShort:
                    if (quote.bid > 0)
                        return quote.bid;
                    break;
                default:
                    throw new UnsupportedOperationException("Order side is not supported : " + order.side);
            }
        return 0.0;
    }

    public double getQty(Order order, Quote quote, SimulatorConfig config) {
        if (quote != null && config.partialFills) {
            switch (order.side) {
                case Buy:
                    if (quote.askSize > 0)
                        return quote.askSize;
                    break;
                case Sell:
                case SellShort:
                    if (quote.bidSize > 0)
                        return quote.bidSize;
                    break;
                default:
                    return order.ordQty;
            }
        }
        return order.ordQty;
    }
}
