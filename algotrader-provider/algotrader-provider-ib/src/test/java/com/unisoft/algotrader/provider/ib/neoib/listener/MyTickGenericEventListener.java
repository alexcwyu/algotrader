package com.unisoft.algotrader.provider.ib.neoib.listener;

import ch.aonyx.broker.ib.api.data.TickGenericEvent;
import ch.aonyx.broker.ib.api.data.TickGenericEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/24/15.
 */
public class MyTickGenericEventListener implements TickGenericEventListener {

    private static final Logger LOG = LogManager.getLogger(MyTickGenericEventListener.class);

    @Override
    public void notify(TickGenericEvent event) {
        LOG.info(event);
    }
}
