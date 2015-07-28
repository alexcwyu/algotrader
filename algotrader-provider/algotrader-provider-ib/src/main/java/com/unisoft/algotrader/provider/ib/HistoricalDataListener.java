package com.unisoft.algotrader.provider.ib;

import ch.aonyx.broker.ib.api.data.historical.HistoricalDataEvent;
import ch.aonyx.broker.ib.api.data.historical.HistoricalDataEventListener;

/**
 * Created by alex on 7/27/15.
 */
public class HistoricalDataListener extends MarketDataListener implements HistoricalDataEventListener {

    public HistoricalDataListener(IBProvider ibProvider) {
        super(ibProvider);
    }

    @Override
    public void notify(HistoricalDataEvent e) {
        publish(e.getId(), e.getDateTime(), e.getOpen(), e.getHigh(), e.getLow(), e.getClose(), e.getVolume());
    }
}
