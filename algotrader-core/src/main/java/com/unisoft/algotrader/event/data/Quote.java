package com.unisoft.algotrader.event.data;

import com.google.common.base.Objects;
import com.lmax.disruptor.EventFactory;

/**
 * Created by alex on 4/12/15.
 */
public class Quote extends MarketData<Quote> {


    public double bid = 0.0;

    public double ask = 0.0;

    public int bidSize = 0;

    public int askSize = 0;

    public Quote(){
    }

    public Quote(
            String instId,
            long dateTime,
            double bid,
            double ask,
            int bidSize,
            int askSize){
        super(instId, dateTime);
        this.bid = bid;
        this.ask = ask;
        this.bidSize = bidSize;
        this.askSize = askSize;
    }

    public Quote(Quote quote){
        super(quote.instId, quote.dateTime);
        this.bid = quote.bid;
        this.ask = quote.ask;
        this.bidSize = quote.bidSize;
        this.askSize = quote.askSize;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "instId=" + instId +
                "dateTime=" + dateTime +
                ", bid=" + bid +
                ", ask=" + ask +
                ", bidSize=" + bidSize +
                ", askSize=" + askSize +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote quote = (Quote) o;
        return Objects.equal(instId, quote.instId) &&
                Objects.equal(dateTime, quote.dateTime) &&
                Objects.equal(bid, quote.bid) &&
                Objects.equal(ask, quote.ask) &&
                Objects.equal(bidSize, quote.bidSize) &&
                Objects.equal(askSize, quote.askSize);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instId, dateTime, bid, ask, bidSize, askSize);
    }

    @Override
    public void on(MarketDataHandler handler){
        handler.onQuote(this);
    }

    public static final EventFactory<Quote> FACTORY = new EventFactory(){
        @Override
        public Quote newInstance() {
            return new Quote();
        }
    };

    @Override
    public void reset(){
        super.reset();
        bid = 0.0;
        ask = 0.0;
        bidSize = 0;
        askSize = 0;
    }

    @Override
    public void copy(Quote quote) {
        this.instId = quote.instId;
        this.dateTime = quote.dateTime;
        this.bid = quote.bid;
        this.ask = quote.ask;
        this.bidSize = quote.bidSize;
        this.askSize = quote.askSize;
    }
}
