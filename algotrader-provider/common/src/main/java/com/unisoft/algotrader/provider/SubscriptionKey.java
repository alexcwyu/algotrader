package com.unisoft.algotrader.provider;

import com.google.common.base.Objects;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;

/**
 * Created by alex on 6/16/15.
 */
public class SubscriptionKey {
    public static enum Type{
        Bar,
        Quote,
        Trade
    }

    public final static int M1_SIZE = 60;
    public final static int M15_SIZE = 60*15;
    public final static int M30_SIZE = 60*30;

    public final static int H1_SIZE = 60*60;
    public final static int H4_SIZE = 60*60*4;

    public final static int DAILY_SIZE = 60*60*24;


    public final Type type;

    public final String instId;

    public final int frequency;

    private SubscriptionKey(Type type, String instId){
        this(type, instId, 0);
    }

    private SubscriptionKey(Type type, String instId, int frequency){
        this.type = type;
        this.instId = instId;
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriptionKey)) return false;
        SubscriptionKey that = (SubscriptionKey) o;
        return Objects.equal(frequency, that.frequency) &&
                Objects.equal(type, that.type) &&
                Objects.equal(instId, that.instId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, instId, frequency);
    }

    @Override
    public String toString() {
        return "SubscriptionKey{" +
                "type=" + type +
                ", instId='" + instId + '\'' +
                ", frequency=" + frequency +
                '}';
    }

    public static SubscriptionKey createBarSubscriptionKey(String instId, int frequency){
        return new SubscriptionKey(Type.Bar, instId, frequency);
    }

    public static SubscriptionKey create1MBarSubscriptionKey(String instId){
        return new SubscriptionKey(Type.Bar, instId, M1_SIZE);
    }

    public static SubscriptionKey create15MBarSubscriptionKey(String instId){
        return new SubscriptionKey(Type.Bar, instId, M15_SIZE);
    }

    public static SubscriptionKey create30MBarSubscriptionKey(String instId){
        return new SubscriptionKey(Type.Bar, instId, M30_SIZE);
    }

    public static SubscriptionKey create1HBarSubscriptionKey(String instId){
        return new SubscriptionKey(Type.Bar, instId, H1_SIZE);
    }

    public static SubscriptionKey create4HBarSubscriptionKey(String instId){
        return new SubscriptionKey(Type.Bar, instId, H4_SIZE);
    }

    public static SubscriptionKey createDailySubscriptionKey(String instId){
        return new SubscriptionKey(Type.Bar, instId, DAILY_SIZE);
    }

    public static SubscriptionKey createTradeSubscriptionKey(String instId){
        return new SubscriptionKey(Type.Trade, instId);
    }

    public static SubscriptionKey createQuoteSubscriptionKey(String instId){
        return new SubscriptionKey(Type.Quote, instId);
    }

    public static SubscriptionKey createSubscriptionKey(Bar bar){
        return new SubscriptionKey(Type.Bar, bar.instId, bar.size);
    }

    public static SubscriptionKey createSubscriptionKey(Quote quote){
        return new SubscriptionKey(Type.Bar, quote.instId);
    }
    public static SubscriptionKey createSubscriptionKey(Trade trade){
        return new SubscriptionKey(Type.Bar, trade.instId);
    }
}
