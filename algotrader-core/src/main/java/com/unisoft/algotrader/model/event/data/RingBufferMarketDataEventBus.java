package com.unisoft.algotrader.model.event.data;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.EventBus;

/**
 * Created by alex on 6/18/15.
 */
public class RingBufferMarketDataEventBus implements EventBus.MarketDataEventBus {

    private final RingBuffer<MarketDataContainer> marketDataRB;

    public RingBufferMarketDataEventBus(RingBuffer<MarketDataContainer> marketDataRB){
        this.marketDataRB = marketDataRB;
    }

    @Override
    public void publishBar(int instId, int size, long dateTime, double open, double high, double low, double close, long volume, long openInt) {

        long sequence = marketDataRB.next();
        MarketDataContainer event = marketDataRB.get(sequence);
        event.reset();
        event.dateTime = dateTime;
        event.instId = instId;
        event.bitset.set(MarketDataContainer.BAR_BIT);

        setBar(event.bar, instId, size, dateTime, open, high, low, close, volume, openInt);

        marketDataRB.publish(sequence);

    }

    @Override
    public void publishQuote(int instId, long dateTime, double bid, double ask, int bidSize, int askSize) {
        long sequence = marketDataRB.next();
        MarketDataContainer event = marketDataRB.get(sequence);
        event.reset();
        event.dateTime = dateTime;
        event.instId = instId;
        event.bitset.set(MarketDataContainer.QUOTE_BIT);

        setQuote(event.quote, instId, dateTime, bid, ask, bidSize, askSize);

        marketDataRB.publish(sequence);
    }

    @Override
    public void publishTrade(int instId, long dateTime, double price, int size) {
        long sequence = marketDataRB.next();
        MarketDataContainer event = marketDataRB.get(sequence);
        event.reset();
        event.dateTime = dateTime;
        event.instId = instId;
        event.bitset.set(MarketDataContainer.TRADE_BIT);

        setTrade(event.trade, instId, dateTime, price, size);

        marketDataRB.publish(sequence);
    }
}
