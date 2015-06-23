package com.unisoft.algotrader.provider.ib.neoib.listener;

import ch.aonyx.broker.ib.api.data.historical.HistoricalDataEvent;
import ch.aonyx.broker.ib.api.data.historical.HistoricalDataEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/24/15.
 */
public class MyHistoricalDataEventListener implements HistoricalDataEventListener {
    private static final Logger LOG = LogManager.getLogger(MyHistoricalDataEventListener.class);
    @Override
    public void notify(HistoricalDataEvent event) {
        LOG.info(event);
    }
}
