package com.unisoft.algotrader.provider.data;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.DataType;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;

import java.util.Date;
import java.util.Objects;

/**
 * Created by alex on 18/7/15.
 */
public class HistoricalSubscriptionKey extends SubscriptionKey {
    public final long fromDate;
    public final long toDate;

    protected HistoricalSubscriptionKey(int providerId, DataType type, long instId, long fromDate, long toDate){
        super(providerId, type, instId, 0);
        this.fromDate = fromDate;
        this.toDate = toDate;

    }

    protected HistoricalSubscriptionKey(int providerId, DataType type, long instId, int barSize, long fromDate, long toDate){
        super(providerId, type, instId, barSize);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoricalSubscriptionKey)) return false;
        if (!super.equals(o)) return false;
        HistoricalSubscriptionKey that = (HistoricalSubscriptionKey) o;
        return Objects.equals(fromDate, that.fromDate) &&
                Objects.equals(toDate, that.toDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fromDate, toDate);
    }

    @Override
    public String toString() {
        return "HistoricalSubscriptionKey{" +
                "fromDate=" + fromDate +
                ", toDate=" + toDate +
                "} " + super.toString();
    }



    public static HistoricalSubscriptionKey createBarSubscriptionKey(int providerId, long instId, int frequency, Date fromDate, Date toDate){
        return new HistoricalSubscriptionKey(providerId, DataType.Bar, instId, frequency, fromDate.getTime(), toDate.getTime());
    }

    public static HistoricalSubscriptionKey createDailySubscriptionKey(int providerId, long instId, Date fromDate, Date toDate){
        return new HistoricalSubscriptionKey(providerId, DataType.Bar, instId, DAILY_SIZE, fromDate.getTime(), toDate.getTime());
    }

    public static HistoricalSubscriptionKey createTradeSubscriptionKey(int providerId, long instId, Date fromDate, Date toDate){
        return new HistoricalSubscriptionKey(providerId, DataType.Trade, instId, fromDate.getTime(), toDate.getTime());
    }

    public static HistoricalSubscriptionKey createQuoteSubscriptionKey(int providerId, long instId, Date fromDate, Date toDate){
        return new HistoricalSubscriptionKey(providerId, DataType.Quote, instId, fromDate.getTime(), toDate.getTime());
    }

    public static HistoricalSubscriptionKey createSubscriptionKey(int providerId, Bar bar, Date fromDate, Date toDate){
        return new HistoricalSubscriptionKey(providerId, DataType.Bar, bar.instId, bar.size, fromDate.getTime(), toDate.getTime());
    }

    public static HistoricalSubscriptionKey createSubscriptionKey(int providerId, Quote quote, Date fromDate, Date toDate){
        return new HistoricalSubscriptionKey(providerId, DataType.Bar, quote.instId, fromDate.getTime(), toDate.getTime());
    }
    public static HistoricalSubscriptionKey createSubscriptionKey(int providerId, Trade trade, Date fromDate, Date toDate){
        return new HistoricalSubscriptionKey(providerId, DataType.Bar, trade.instId, fromDate.getTime(), toDate.getTime());
    }

    public static HistoricalSubscriptionKey createBarSubscriptionKey(int providerId, long instId, int frequency, long fromDate, long toDate){
        return new HistoricalSubscriptionKey(providerId, DataType.Bar, instId, frequency, fromDate, toDate);
    }

    public static HistoricalSubscriptionKey createDailySubscriptionKey(int providerId, long instId, long fromDate, long toDate){
        return new HistoricalSubscriptionKey(providerId, DataType.Bar, instId, DAILY_SIZE, fromDate, toDate);
    }

    public static HistoricalSubscriptionKey createTradeSubscriptionKey(int providerId, long instId, long fromDate, long toDate){
        return new HistoricalSubscriptionKey(providerId, DataType.Trade, instId, fromDate, toDate);
    }

    public static HistoricalSubscriptionKey createQuoteSubscriptionKey(int providerId, long instId, long fromDate, long toDate){
        return new HistoricalSubscriptionKey(providerId, DataType.Quote, instId, fromDate, toDate);
    }

    public static HistoricalSubscriptionKey createSubscriptionKey(int providerId, Bar bar, long fromDate, long toDate){
        return new HistoricalSubscriptionKey(providerId, DataType.Bar, bar.instId, bar.size, fromDate, toDate);
    }

    public static HistoricalSubscriptionKey createSubscriptionKey(int providerId, Quote quote, long fromDate, long toDate){
        return new HistoricalSubscriptionKey(providerId, DataType.Bar, quote.instId, fromDate, toDate);
    }
    public static HistoricalSubscriptionKey createSubscriptionKey(int providerId, Trade trade, long fromDate, long toDate){
        return new HistoricalSubscriptionKey(providerId, DataType.Bar, trade.instId, fromDate, toDate);
    }
}
