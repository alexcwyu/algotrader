package com.unisoft.algotrader.event.bus;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.model.event.bus.MarketDepthEventBus;
import com.unisoft.algotrader.model.event.data.MDOperation;
import com.unisoft.algotrader.model.event.data.MDSide;
import com.unisoft.algotrader.model.event.data.MarketDepth;

/**
 * Created by alex on 9/20/15.
 */
public class RingBufferMarketDepthEventBus implements MarketDepthEventBus {

    private final RingBuffer<MarketDepth> marketDepthRB;

    public RingBufferMarketDepthEventBus(RingBuffer<MarketDepth> marketDepthRB){
        this.marketDepthRB = marketDepthRB;
    }

    private long getNextSeq(){
        long seq = marketDepthRB.next();
        return seq;
    }

    private MarketDepth getNextEventContainer(long seq){
        MarketDepth marketDepth = marketDepthRB.get(seq);
        marketDepth.reset();
        return marketDepth;
    }

    @Override
    public void publishMarketDepth(long instId, long dateTime, int providerId, int position, MDOperation operation, MDSide side, double price, int size) {
        long seq = getNextSeq();
        MarketDepth event = getNextEventContainer(seq);

        event.instId = instId;
        event.dateTime = dateTime;
        event.providerId = providerId;
        event.position = position;
        event.operation = operation;
        event.side = side;
        event.price = price;
        event.size = size;
        marketDepthRB.publish(seq);
    }
}
