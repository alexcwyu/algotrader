package com.unisoft.algotrader.provider.ib.api.event;


/**
 * Created by alex on 8/26/15.
 */
public class RealTimeBarEvent extends IBEvent<RealTimeBarEvent>  {

    public final long timestamp;
    public final double open;
    public final double high;
    public final double low;
    public final double close;
    public final long volume;
    public final double weightedAveragePrice;
    public final int tradeNumber;

    public RealTimeBarEvent(final String requestId, final long timestamp, final double open, final double high,
                            final double low, final double close, final long volume, final double weightedAveragePrice,
                            final int tradeNumber){
        super(requestId);
        this.timestamp = timestamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.weightedAveragePrice = weightedAveragePrice;
        this.tradeNumber = tradeNumber;
    }
}