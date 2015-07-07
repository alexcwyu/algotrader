package com.unisoft.algotrader.model.event.data;

import com.google.common.base.Objects;
import com.lmax.disruptor.EventFactory;
import org.msgpack.annotation.Message;

import java.util.BitSet;

/**
 * Created by alex on 4/12/15.
 */
@Message
public class MarketDataContainer extends MarketData<MarketDataContainer> {

    public static final int BAR_BIT = 1;
    public static final int QUOTE_BIT = 2;
    public static final int TRADE_BIT = 3;

    public BitSet bitset = new BitSet();

    public int size = -1;

    public Bar bar = new Bar();

    public Quote quote = new Quote();

    public Trade trade = new Trade();

    public MarketDataContainer(){

    }

    public MarketDataContainer(MarketDataContainer container) {
        this(container.instId, container.dateTime, container.size, container.bar, container.quote, container.trade);
    }

    public MarketDataContainer(
            int instId,
            long dateTime,
            int size,
            Bar bar,
            Quote quote,
            Trade trade) {
        super(instId, dateTime);
        this.size = size;

        setBar(bar);
        setTrade(trade);
        setQuote(quote);
    }

    public boolean hasBar(){
        return bitset.get(BAR_BIT);
    }

    public boolean hasQuote(){
        return bitset.get(QUOTE_BIT);
    }

    public boolean hasTrade(){
        return bitset.get(TRADE_BIT);
    }



    public void setBar(Bar bar){

        this.bar.reset();
        if (bar != null) {
            this.instId = bar.instId;
            this.dateTime = bar.dateTime;
            this.bar.copy(bar);
            bitset.set(BAR_BIT);
        }

    }

    public void setTrade(Trade trade){
        this.trade.reset();
        if (trade != null) {
            this.instId = trade.instId;
            this.dateTime = trade.dateTime;
            this.trade.copy(trade);
            bitset.set(TRADE_BIT);
        }
    }

    public void setQuote(Quote quote){
        this.quote.reset();
        if (quote != null) {
            this.instId = quote.instId;
            this.dateTime = quote.dateTime;
            this.quote.copy(quote);
            bitset.set(QUOTE_BIT);
        }

    }



    @Override
    public String toString() {
        return "Bar{" +
                "instId=" + instId +
                "dateTime=" + dateTime +
                ", size=" + size +
                ", bar=" + bar +
                ", quote=" + quote +
                ", trade=" + trade +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketDataContainer container = (MarketDataContainer) o;
        return Objects.equal(instId, container.instId) &&
                Objects.equal(dateTime, container.dateTime) &&
                Objects.equal(size, container.size) &&
                Objects.equal(bar, container.bar) &&
                Objects.equal(quote, container.quote) &&
                Objects.equal(trade, container.trade);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instId, dateTime, size, bar, quote, trade);
    }

    public static final EventFactory<MarketDataContainer> FACTORY = new EventFactory(){
        @Override
        public MarketDataContainer newInstance() {
            return new MarketDataContainer();
        }
    };

    @Override
    public void on(MarketDataHandler handler){
        handler.onMarketDataContainer(this);
    }

    @Override
    public void reset(){
        super.reset();
        size =-1;
        bitset.clear();
        bar.reset();
        quote.reset();
        trade.reset();
    }

    @Override
    public void copy(MarketDataContainer event) {
        this.instId = event.instId;
        this.dateTime = event.dateTime;
        this.size = event.size;

        if (event.bitset.get(BAR_BIT))
            setBar(event.bar);
        if (event.bitset.get(TRADE_BIT))
            setTrade(event.trade);
        if (event.bitset.get(QUOTE_BIT))
            setQuote(event.quote);
    }
}
