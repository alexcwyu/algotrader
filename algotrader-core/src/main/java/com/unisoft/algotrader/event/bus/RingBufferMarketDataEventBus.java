package com.unisoft.algotrader.event.bus;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.bus.MarketDataEventBus;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;

/**
 * Created by alex on 6/18/15.
 */
public class RingBufferMarketDataEventBus implements MarketDataEventBus {

    private final RingBuffer<MarketDataContainer> marketDataRB;

    public RingBufferMarketDataEventBus(RingBuffer<MarketDataContainer> marketDataRB){
        this.marketDataRB = marketDataRB;
    }

    private long getNextSeq(){
        long seq = marketDataRB.next();
        return seq;
    }

    private MarketDataContainer getNextEventContainer(long seq){
        MarketDataContainer container = marketDataRB.get(seq);
        container.reset();
        return container;
    }


    @Override
    public void publishBar(long instId, int size, long dateTime, double open, double high, double low, double close, long volume, long openInt, boolean endOfData) {
        long seq = getNextSeq();
        MarketDataContainer event = getNextEventContainer(seq);
        event.dateTime = dateTime;
        event.instId = instId;
        event.bitset.set(MarketDataContainer.BAR_BIT);
        event.endOfData = endOfData;
        setBar(event.bar, instId, size, dateTime, open, high, low, close, volume, openInt);

        marketDataRB.publish(seq);

    }

    @Override
    public void publishQuote(long instId, long dateTime, double bid, double ask, int bidSize, int askSize, boolean endOfData) {
        long seq = getNextSeq();
        MarketDataContainer event = getNextEventContainer(seq);
        event.dateTime = dateTime;
        event.instId = instId;
        event.bitset.set(MarketDataContainer.QUOTE_BIT);
        event.endOfData = endOfData;
        setQuote(event.quote, instId, dateTime, bid, ask, bidSize, askSize);

        marketDataRB.publish(seq);
    }

    @Override
    public void publishTrade(long instId, long dateTime, double price, int size, boolean endOfData) {
        long seq = getNextSeq();
        MarketDataContainer event = getNextEventContainer(seq);
        event.dateTime = dateTime;
        event.instId = instId;
        event.bitset.set(MarketDataContainer.TRADE_BIT);
        event.endOfData = endOfData;
        setTrade(event.trade, instId, dateTime, price, size);

        marketDataRB.publish(seq);
    }

    public void onCompleted(){

    }
}
