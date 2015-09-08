package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.model.data.MarketDataType;

/**
 * Created by alex on 8/26/15.
 */
public class MarketDataTypeEvent extends IBEvent<MarketDataTypeEvent>  {

    public final MarketDataType marketDataType;

    public MarketDataTypeEvent(final long requestId, final MarketDataType marketDataType){
        super(requestId);
        this.marketDataType = marketDataType;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onMarketDataTypeEvent(this);
    }

    @Override
    public String toString() {
        return "MarketDataTypeEvent{" +
                "marketDataType=" + marketDataType +
                "} " + super.toString();
    }
}