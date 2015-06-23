package com.unisoft.algotrader.provider.ib.neoib.listener;

import ch.aonyx.broker.ib.api.data.bar.RealTimeBarEvent;
import ch.aonyx.broker.ib.api.data.bar.RealTimeBarEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/24/15.
 */
public class MyRealTimeBarEventListener implements RealTimeBarEventListener {
    private static final Logger LOG = LogManager.getLogger(MyRealTimeBarEventListener.class);

    @Override
    public void notify(RealTimeBarEvent event) {
        LOG.info(event);
    }
}
