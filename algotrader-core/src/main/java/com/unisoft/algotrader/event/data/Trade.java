package com.unisoft.algotrader.event.data;

import com.google.common.base.Objects;

/**
 * Created by alex on 4/12/15.
 */
public class Trade extends MarketData<Trade> {
    public double price = 0.0;
    public int size = 0;
    public Trade(){

    }

    public Trade(int instId,
                 long dateTime,
                 double price,
                 int size){
        super(instId, dateTime);
        this.price = price;
        this.size = size;
    }

    public Trade(Trade trade){
        super(trade.instId, trade.dateTime);
        this.price = trade.price;
        this.size = trade.size;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "instId=" + instId +
                "dateTime=" + dateTime +
                ", marketPrice=" + price +
                ", size=" + size +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trade)) return false;
        Trade trade = (Trade) o;
        return Objects.equal(instId, trade.instId) &&
                Objects.equal(dateTime, trade.dateTime) &&
                Objects.equal(price, trade.price) &&
                Objects.equal(size, trade.size);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instId, dateTime, price, size);
    }

    @Override
    public void on(MarketDataHandler handler){
        handler.onTrade(this);
    }

    @Override
    public void reset(){
        super.reset();
        price = 0.0;
        size = 0;
    }

    @Override
    public void copy(Trade trade) {
        this.instId = trade.instId;
        this.dateTime = trade.dateTime;
        this.price = trade.price;
        this.size = trade.size;
    }
}
