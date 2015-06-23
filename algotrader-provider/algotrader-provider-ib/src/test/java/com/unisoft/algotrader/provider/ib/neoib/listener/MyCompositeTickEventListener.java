package com.unisoft.algotrader.provider.ib.neoib.listener;

import ch.aonyx.broker.ib.api.data.CompositeTickEvent;
import ch.aonyx.broker.ib.api.data.CompositeTickEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/24/15.
 */
public class MyCompositeTickEventListener implements CompositeTickEventListener {
    private static final Logger LOG = LogManager.getLogger(MyCompositeTickEventListener.class);
    @Override
    public void notify(CompositeTickEvent event) {
        LOG.info(event);
    }
}
