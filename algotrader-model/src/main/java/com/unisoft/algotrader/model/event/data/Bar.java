package com.unisoft.algotrader.model.event.data;

import com.google.common.base.Objects;
import org.msgpack.annotation.Message;

/**
 * Created by alex on 4/12/15.
 */
@Message
public class Bar extends MarketData<Bar> {

    public int size = -1;

    public double open = 0.0;

    public double high = 0.0;

    public double low = 0.0;

    public double close = 0.0;

    public long volume = 0;

    public long openInt = 0;

    public Bar(){

    }

    public Bar(Bar bar) {
        super(bar.instId, bar.dateTime);
        this.size = bar.size;
        this.open = bar.open;
        this.high = bar.high;
        this.low = bar.low;
        this.close = bar.close;
        this.volume = bar.volume;
        this.openInt = bar.openInt;
    }


    public Bar(
            long instId,
            int size,
            long dateTime,
            double open,
            double high,
            double low,
            double close) {
        this(instId, size, dateTime, open, high, low, close, 0, 0);
    }

    public Bar(
            long instId,
            int size,
            long dateTime,
            double open,
            double high,
            double low,
            double close,
            long volume) {
        this(instId, size, dateTime, open, high, low, close, volume, 0);
    }

    public Bar(
            long instId,
            int size,
            long dateTime,
            double open,
            double high,
            double low,
            double close,
            long volume,
            long openInt) {
        super(instId, dateTime);
        this.size = size;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.openInt = openInt;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "instId=" + instId +
                ", dateTime=" + dateTime +
                ", size=" + size +
                ", high=" + high +
                ", low=" + low +
                ", open=" + open +
                ", close=" + close +
                ", volume=" + volume +
                ", openInt=" + openInt +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bar bar = (Bar) o;
        return Objects.equal(instId, bar.instId) &&
                Objects.equal(dateTime, bar.dateTime) &&
                Objects.equal(size, bar.size) &&
                Objects.equal(high, bar.high) &&
                Objects.equal(low, bar.low) &&
                Objects.equal(open, bar.open) &&
                Objects.equal(close, bar.close) &&
                Objects.equal(volume, bar.volume) &&
                Objects.equal(openInt, bar.openInt);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instId, dateTime, size, high, low, open, close, volume, openInt);
    }

    @Override
    public void on(MarketDataHandler handler){
        handler.onBar(this);
    }

    @Override
    public void reset(){
        super.reset();
        size =-1;
        high = 0.0;
        low = 0.0;
        open = 0.0;
        close = 0.0;
        volume = 0;
        openInt = 0;
    }

    @Override
    public void copy(Bar bar) {
        this.instId = bar.instId;
        this.dateTime = bar.dateTime;
        this.size = bar.size;
        this.high = bar.high;
        this.low = bar.low;
        this.open = bar.open;
        this.close = bar.close;
        this.volume = bar.volume;
        this.openInt = bar.openInt;
    }
}
