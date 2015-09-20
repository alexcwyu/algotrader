package com.unisoft.algotrader.model.event.bus;

import com.unisoft.algotrader.model.event.data.MDOperation;
import com.unisoft.algotrader.model.event.data.MDSide;
import com.unisoft.algotrader.model.event.data.MarketDepth;

/**
 * Created by alex on 9/20/15.
 */
public interface MarketDepthEventBus {
    void publishMarketDepth(long instId, long dateTime,
                            int providerId, int position,
                            MDOperation operation, MDSide side,
                            double price, int size);

    default void setMarketDepth(MarketDepth md, long instId, long dateTime,
                                int providerId, int position,
                                MDOperation operation, MDSide side,
                                double price, int size) {
        md.reset();
        md.instId = instId;
        md.dateTime = dateTime;
        md.providerId = providerId;
        md.position = position;
        md.operation = operation;
        md.side = side;
        md.price = price;
        md.size = size;
    }
}
