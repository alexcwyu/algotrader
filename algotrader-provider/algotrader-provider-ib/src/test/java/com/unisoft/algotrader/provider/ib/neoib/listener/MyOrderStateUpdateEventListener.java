package com.unisoft.algotrader.provider.ib.neoib.listener;

import ch.aonyx.broker.ib.api.order.OrderStateUpdateEvent;
import ch.aonyx.broker.ib.api.order.OrderStateUpdateEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/24/15.
 */
public class MyOrderStateUpdateEventListener implements OrderStateUpdateEventListener {
    private static final Logger LOG = LogManager.getLogger(MyOrderStateUpdateEventListener.class);
    @Override
    public void notify(OrderStateUpdateEvent event) {
        LOG.info(event);
    }
}
