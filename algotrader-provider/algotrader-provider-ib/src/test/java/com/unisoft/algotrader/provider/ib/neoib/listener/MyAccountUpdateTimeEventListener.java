package com.unisoft.algotrader.provider.ib.neoib.listener;

import ch.aonyx.broker.ib.api.account.AccountUpdateTimeEvent;
import ch.aonyx.broker.ib.api.account.AccountUpdateTimeEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/24/15.
 */
public class MyAccountUpdateTimeEventListener implements AccountUpdateTimeEventListener {

    private static final Logger LOG = LogManager.getLogger(MyAccountUpdateTimeEventListener.class);
    @Override
    public void notify(AccountUpdateTimeEvent event) {
        LOG.info(event);
    }
}
