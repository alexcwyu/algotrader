package com.unisoft.algotrader.provider.ib.api.event;


import java.util.List;

/**
 * Created by alex on 8/26/15.
 */
public class MarketScannerDataListEvent extends IBEvent<MarketScannerDataListEvent>  {

    public final List<MarketScannerDataEvent> marketScannerDataEvents;
    public MarketScannerDataListEvent(final long requestId, List<MarketScannerDataEvent> marketScannerDataEvents){
        super(requestId);
        this.marketScannerDataEvents = marketScannerDataEvents;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onMarketScannerDataListEvent(this);
    }

    @Override
    public String toString() {
        return "MarketScannerDataListEvent{" +
                "marketScannerDataEvents=" + marketScannerDataEvents +
                "} " + super.toString();
    }
}