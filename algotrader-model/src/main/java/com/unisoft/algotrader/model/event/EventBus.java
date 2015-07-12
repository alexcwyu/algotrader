package com.unisoft.algotrader.model.event;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;

/**
 * Created by alex on 6/18/15.
 */
public interface EventBus {
    interface BarEventBus extends EventBus{
        void publishBar(int instId,
                               int size,
                               long dateTime,
                               double open,
                               double high,
                               double low,
                               double close,
                               long volume,
                               long openInt);

        default void setBar(Bar bar, int instId,
                            int size,
                            long dateTime,
                            double open,
                            double high,
                            double low,
                            double close,
                            long volume,
                            long openInt){
            bar.reset();
            bar.instId = instId;
            bar.dateTime = dateTime;
            bar.size = size;
            bar.open = open;
            bar.high = high;
            bar.low = low;
            bar.close = close;
            bar.volume = volume;
            bar.openInt = openInt;
        }
    }

    interface QuoteEventBus extends EventBus{
        void publishQuote( int instId,
                           long dateTime,
                           double bid,
                           double ask,
                           int bidSize,
                           int askSize);

        default void setQuote(Quote quote, int instId,
                         long dateTime,
                         double bid,
                         double ask,
                         int bidSize,
                         int askSize){
            quote.reset();
            quote.instId = instId;
            quote.dateTime = dateTime;
            quote.bid = bid;
            quote.ask = ask;
            quote.bidSize = bidSize;
            quote.askSize = askSize;

        }
    }

    interface TradeEventBus extends EventBus{
        void publishTrade(int instId,
                          long dateTime,
                          double price,
                          int size);

        default void setTrade(Trade trade, int instId,
                              long dateTime,
                              double price,
                              int size){
            trade.reset();
            trade.instId = instId;
            trade.dateTime = dateTime;
            trade.price = price;
            trade.size = size;
        }
    }

    interface MarketDataEventBus extends TradeEventBus, BarEventBus, QuoteEventBus{

    }
}
