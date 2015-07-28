package com.unisoft.algotrader.provider.ib.neoib.listener;

import ch.aonyx.broker.ib.api.data.historical.HistoricalDataEventListEvent;
import ch.aonyx.broker.ib.api.data.historical.HistoricalDataEventListEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/24/15.
 */
public class MyHistoricalDataEventListEventListener implements HistoricalDataEventListEventListener {
    private static final Logger LOG = LogManager.getLogger(MyHistoricalDataEventListEventListener.class);

    @Override
    public void notify(HistoricalDataEventListEvent event) {
        if (event != null) {
            event.getHistoricalDataEvents().forEach(e -> LOG.info(e));
        }
    }
}
