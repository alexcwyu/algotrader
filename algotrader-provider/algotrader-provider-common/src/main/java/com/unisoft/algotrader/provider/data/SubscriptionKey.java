package com.unisoft.algotrader.provider.data;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.DataType;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;

/**
 * Created by alex on 6/16/15.
 */
public class SubscriptionKey {
    public final static int M1_SIZE = 60;
    public final static int M15_SIZE = 60*15;
    public final static int M30_SIZE = 60*30;

    public final static int H1_SIZE = 60*60;
    public final static int H4_SIZE = 60*60*4;

    public final static int DAILY_SIZE = 60*60*24;

    public final int providerId;
    
    public final DataType type;

    public final long instId;

    public final int barSize;


    protected SubscriptionKey(int providerId, DataType type, long instId){
        this(providerId, type, instId, 0);
    }

    protected SubscriptionKey(int providerId, DataType type, long instId, int barSize){
        this.providerId = providerId;
        this.type = type;
        this.instId = instId;
        this.barSize = barSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriptionKey)) return false;
        SubscriptionKey that = (SubscriptionKey) o;
        return com.google.common.base.Objects.equal(instId, that.instId) &&
                com.google.common.base.Objects.equal(barSize, that.barSize) &&
                com.google.common.base.Objects.equal(providerId, that.providerId) &&
                com.google.common.base.Objects.equal(type, that.type);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode( providerId, type, instId, barSize);
    }

    @Override
    public String toString() {
        return "SubscriptionKey{" +
                "providerId='" + providerId + '\'' +
                ", type=" + type +
                ", instId=" + instId +
                ", barSize=" + barSize +
                "} " + super.toString();
    }

    public static SubscriptionKey createBarSubscriptionKey(int providerId, long instId, int frequency){
        return new SubscriptionKey(providerId, DataType.Bar, instId, frequency);
    }

    public static SubscriptionKey create1MBarSubscriptionKey(int providerId, long instId){
        return new SubscriptionKey(providerId, DataType.Bar, instId, M1_SIZE);
    }

    public static SubscriptionKey create15MBarSubscriptionKey(int providerId, long instId){
        return new SubscriptionKey(providerId, DataType.Bar, instId, M15_SIZE);
    }

    public static SubscriptionKey create30MBarSubscriptionKey(int providerId, long instId){
        return new SubscriptionKey(providerId, DataType.Bar, instId, M30_SIZE);
    }

    public static SubscriptionKey create1HBarSubscriptionKey(int providerId, long instId){
        return new SubscriptionKey(providerId, DataType.Bar, instId, H1_SIZE);
    }

    public static SubscriptionKey create4HBarSubscriptionKey(int providerId, long instId){
        return new SubscriptionKey(providerId, DataType.Bar, instId, H4_SIZE);
    }

    public static SubscriptionKey createDailySubscriptionKey(int providerId, long instId){
        return new SubscriptionKey(providerId, DataType.Bar, instId, DAILY_SIZE);
    }

    public static SubscriptionKey createTradeSubscriptionKey(int providerId, long instId){
        return new SubscriptionKey(providerId, DataType.Trade, instId);
    }

    public static SubscriptionKey createQuoteSubscriptionKey(int providerId, long instId){
        return new SubscriptionKey(providerId, DataType.Quote, instId);
    }

    public static SubscriptionKey createSubscriptionKey(int providerId, Bar bar){
        return new SubscriptionKey(providerId, DataType.Bar, bar.instId, bar.size);
    }

    public static SubscriptionKey createSubscriptionKey(int providerId, Quote quote){
        return new SubscriptionKey(providerId, DataType.Bar, quote.instId);
    }
    public static SubscriptionKey createSubscriptionKey(int providerId, Trade trade){
        return new SubscriptionKey(providerId, DataType.Bar, trade.instId);
    }
}
