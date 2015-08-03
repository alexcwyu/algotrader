package com.unisoft.algotrader.provider.execution.simulation;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketData;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.event.execution.Order;

/**
 * Created by alex on 6/6/15.
 */
public abstract class MarketDataProcessor<T extends MarketData> {

    public abstract double getPrice(Order order, T marketData, SimulatorConfig config);

    public abstract double getQty(Order order, T marketData, SimulatorConfig config);

    public static class BarProcessor extends MarketDataProcessor<Bar>{

        public double getPrice(Order order, Bar bar, SimulatorConfig config){
            switch (config.fillOnBarMode) {
                case LastBarClose:
                case NextBarClose:
                    return bar.close;
                case NextBarOpen:
                    return bar.open;
            }
            return 0.0;
        }

        public double getQty(Order order, Bar bar, SimulatorConfig config){
            return order.ordQty;
        }
    }

    public static class TradeProcessor extends MarketDataProcessor<Trade>{

        public double getPrice(Order order, Trade trade, SimulatorConfig config){
            if (trade.price >0){
                return trade.price;
            }
            return 0.0;
        }
        public double getQty(Order order, Trade trade, SimulatorConfig config){
            return order.ordQty;
        }
    }


    public static class QuoteProcessor extends MarketDataProcessor<Quote>{

        public double getPrice(Order order, Quote quote, SimulatorConfig config){
            switch (order.side){
                case Buy:
                //case BuyMinus:
                    if (quote.ask > 0)
                        return quote.ask;
                    break;
                case Sell:
                case SellShort:
                    if (quote.bid >0)
                        return quote.bid;
                    break;
                default:
                    throw new UnsupportedOperationException("Order side is not supported : " + order.side);
            }
            return 0.0;
        }
        public double getQty(Order order, Quote quote, SimulatorConfig config){
            if (quote != null && config.partialFills){
                switch (order.side){
                    case Buy:
                        if (quote.askSize > 0)
                            return quote.askSize;
                        break;
                    case Sell:
                    case SellShort:
                        if (quote.bidSize >0)
                            return quote.bidSize;
                        break;
                    default:
                        return order.ordQty;
                }
            }
            return order.ordQty;
        }
    }

}
