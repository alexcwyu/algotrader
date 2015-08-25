package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class HistoricalDataEvent extends IBEvent<HistoricalDataEvent>  {

    public final String dateTime;
    public final double open;
    public final double high;
    public final double low;
    public final double close;
    public final int volume;
    public final int tradeNumber;
    public final double weightedAveragePrice;
    public final boolean hasGap;
    public HistoricalDataEvent(final String requestId, final String dateTime, final double open, final double high,
                               final double low, final double close, final int volume, final int tradeNumber,
                               final double weightedAveragePrice, final boolean hasGap){
        super(requestId);
        this.dateTime = dateTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.tradeNumber = tradeNumber;
        this.weightedAveragePrice = weightedAveragePrice;
        this.hasGap = hasGap;
    }
}