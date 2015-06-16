package com.unisoft.algotrader.integration;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.MarketDataContainer;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;

/**
 * Created by alex on 6/8/15.
 */
public class DataPublisher {

    private final RingBuffer<MarketDataContainer> rb;

    public DataPublisher(RingBuffer<MarketDataContainer> rb){
        this.rb = rb;
    }

    public void publishQuote(Quote quote){
        publishQuote(quote.instId, quote.dateTime, quote.bid, quote.ask, quote.bidSize, quote.askSize);
    }

    public void publishQuote(String instId,
                             long dateTime,
                             double bid,
                             double ask,
                             int bidSize,
                             int askSize){

        long sequence = rb.next();

        MarketDataContainer event = rb.get(sequence);
        event.reset();
        event.bitset.set(MarketDataContainer.QUOTE_BIT);
        event.dateTime = dateTime;
        event.instId = instId;
        Quote quote = event.quote;
        quote.instId = instId;
        quote.dateTime = dateTime;
        quote.bid = bid;
        quote.ask = ask;
        quote.bidSize = bidSize;
        quote.askSize = askSize;

        rb.publish(sequence);
    }

    public void publishTrade(Trade trade){
        publishTrade(trade.instId, trade.dateTime, trade.price, trade.size);
    }


    public void publishTrade(String instId,
                             long dateTime,
                             double price,
                             int size){

        long sequence = rb.next();

        MarketDataContainer event = rb.get(sequence);
        event.reset();
        event.bitset.set(MarketDataContainer.TRADE_BIT);
        event.dateTime = dateTime;
        event.instId = instId;
        Trade trade = event.trade;
        trade.instId = instId;
        trade.dateTime = dateTime;
        trade.price = price;
        trade.size = size;
        rb.publish(sequence);
    }

    public void publishBar(Bar bar){
        publishBar(bar.instId, bar.dateTime, bar.size, bar.high, bar.low, bar.open, bar.close, bar.volume, bar.openInt);
    }
    public void publishBar(String instId,
                           long dateTime,
                           int size,
                           double high,
                           double low,
                           double open,
                           double close,
                           long volume,
                           long openInt){

        long sequence = rb.next();

        MarketDataContainer event = rb.get(sequence);
        event.reset();
        event.bitset.set(MarketDataContainer.BAR_BIT);
        event.dateTime = dateTime;
        event.instId = instId;
        Bar bar = event.bar;
        bar.instId = instId;
        bar.dateTime = dateTime;
        bar.size = size;
        bar.high = high;
        bar.low = low;
        bar.open = open;
        bar.close = close;
        bar.volume = volume;
        bar.openInt = openInt;
        rb.publish(sequence);
    }

}
