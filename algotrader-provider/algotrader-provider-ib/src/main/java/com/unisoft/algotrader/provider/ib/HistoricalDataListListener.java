package com.unisoft.algotrader.provider.ib;

import ch.aonyx.broker.ib.api.Id;
import ch.aonyx.broker.ib.api.data.historical.HistoricalDataEventListEvent;
import ch.aonyx.broker.ib.api.data.historical.HistoricalDataEventListEventListener;

/**
 * Created by alex on 7/27/15.
 */
public class HistoricalDataListListener extends MarketDataListener implements HistoricalDataEventListEventListener {

    public HistoricalDataListListener(IBProvider ibProvider) {
        super(ibProvider);
    }

    @Override
    public void notify(HistoricalDataEventListEvent event) {
        event.getHistoricalDataEvents().forEach(e -> publish(e.getId(), e.getDateTime(), e.getOpen(), e.getHigh(), e.getLow(), e.getClose(), e.getVolume()));
    }
}
