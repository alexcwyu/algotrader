package com.unisoft.algotrader.provider.ib.neoib.listener;

import ch.aonyx.broker.ib.api.data.TickSizeEvent;
import ch.aonyx.broker.ib.api.data.TickSizeEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/24/15.
 */
public class MyTickSizeEventListener implements TickSizeEventListener {
    private static final Logger LOG = LogManager.getLogger(MyTickSizeEventListener.class);

    @Override
    public void notify(TickSizeEvent event) {
        LOG.info(event);
    }
}
