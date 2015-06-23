package com.unisoft.algotrader.provider.ib.neoib.listener;

import ch.aonyx.broker.ib.api.account.PortfolioUpdateEvent;
import ch.aonyx.broker.ib.api.account.PortfolioUpdateEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/24/15.
 */
public class MyPortfolioUpdateEventListener implements PortfolioUpdateEventListener {
    private static final Logger LOG = LogManager.getLogger(MyPortfolioUpdateEventListener.class);

    @Override
    public void notify(PortfolioUpdateEvent event) {
        LOG.info(event);
    }
}
