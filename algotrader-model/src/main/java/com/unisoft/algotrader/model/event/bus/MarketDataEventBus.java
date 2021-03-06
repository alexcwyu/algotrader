package com.unisoft.algotrader.model.event.bus;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;

/**
 * Created by alex on 9/20/15.
 */
public interface MarketDataEventBus {
    void publishBar(long instId,
                    int size,
                    long dateTime,
                    double open,
                    double high,
                    double low,
                    double close,
                    long volume,
                    long openInt);

    default void setBar(Bar bar, long instId,
                        int size,
                        long dateTime,
                        double open,
                        double high,
                        double low,
                        double close,
                        long volume,
                        long openInt) {
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

    void publishQuote(long instId,
                      long dateTime,
                      double bid,
                      double ask,
                      int bidSize,
                      int askSize);

    default void setQuote(Quote quote, long instId,
                          long dateTime,
                          double bid,
                          double ask,
                          int bidSize,
                          int askSize) {
        quote.reset();
        quote.instId = instId;
        quote.dateTime = dateTime;
        quote.bid = bid;
        quote.ask = ask;
        quote.bidSize = bidSize;
        quote.askSize = askSize;

    }

    void publishTrade(long instId,
                      long dateTime,
                      double price,
                      int size);

    default void setTrade(Trade trade, long instId,
                          long dateTime,
                          double price,
                          int size) {
        trade.reset();
        trade.instId = instId;
        trade.dateTime = dateTime;
        trade.price = price;
        trade.size = size;
    }
}
