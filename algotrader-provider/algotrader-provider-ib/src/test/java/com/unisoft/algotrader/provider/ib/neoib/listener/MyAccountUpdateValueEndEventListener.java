package com.unisoft.algotrader.provider.ib.neoib.listener;

import ch.aonyx.broker.ib.api.account.AccountUpdateValueEndEvent;
import ch.aonyx.broker.ib.api.account.AccountUpdateValueEndEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/24/15.
 */
public class MyAccountUpdateValueEndEventListener implements AccountUpdateValueEndEventListener {

    private static final Logger LOG = LogManager.getLogger(MyAccountUpdateValueEndEventListener.class);

    @Override
    public void notify(AccountUpdateValueEndEvent event) {
        LOG.info(event);
    }
}
