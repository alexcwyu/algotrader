package com.unisoft.algotrader.model.event.data;

import com.google.common.base.Objects;
import com.unisoft.algotrader.model.event.Event;

/**
 * Created by alex on 9/16/15.
 */
public class MarketDepth implements Event<MarketDepthHandler, MarketDepth> {
    public long instId = -1;
    public long dateTime = -1;
    public int providerId = 0;
    public int position = 0;
    public MDOperation operation = null;
    public MDSide side = null;
    public double price  = 0;
    public int size = 0;

    public MarketDepth(){

    }

    public MarketDepth(long instId, long dateTime,
                       int providerId, int position,
                       MDOperation operation, MDSide side,
                       double price, int size) {
        this.instId = instId;
        this.dateTime = dateTime;
        this.providerId = providerId;
        this.position = position;
        this.operation = operation;
        this.side = side;
        this.price = price;
        this.size = size;
    }

    public MarketDepth(MarketDepth marketDepth){
        copy(marketDepth);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketDepth)) return false;
        MarketDepth that = (MarketDepth) o;
        return Objects.equal(instId, that.instId) &&
                Objects.equal(dateTime, that.dateTime) &&
                Objects.equal(providerId, that.providerId) &&
                Objects.equal(position, that.position) &&
                Objects.equal(price, that.price) &&
                Objects.equal(size, that.size) &&
                Objects.equal(operation, that.operation) &&
                Objects.equal(side, that.side);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instId, dateTime, providerId, position, operation, side, price, size);
    }

    @Override
    public void on(MarketDepthHandler handler) {
        handler.onMarketDepth(this);
    }


    @Override
    public void reset(){
        providerId = 0;
        position = 0;
        operation = null;
        side = null;
        price  = 0;
        size = 0;
    }

    public void copy(MarketDepth marketDepth) {
        this.instId = marketDepth.instId;
        this.dateTime = marketDepth.dateTime;
        this.providerId = marketDepth.providerId;
        this.position = marketDepth.position;
        this.operation = marketDepth.operation;
        this.side = marketDepth.side;
        this.price = marketDepth.price;
        this.size = marketDepth.size;
    }
}
