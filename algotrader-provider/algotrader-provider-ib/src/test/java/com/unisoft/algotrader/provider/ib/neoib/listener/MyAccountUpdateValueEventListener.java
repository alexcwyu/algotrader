package com.unisoft.algotrader.provider.ib.neoib.listener;

import ch.aonyx.broker.ib.api.account.AccountUpdateValueEvent;
import ch.aonyx.broker.ib.api.account.AccountUpdateValueEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/24/15.
 */
public class MyAccountUpdateValueEventListener implements AccountUpdateValueEventListener {
    private static final Logger LOG = LogManager.getLogger(MyAccountUpdateValueEventListener.class);

    @Override
    public void notify(AccountUpdateValueEvent event) {
        LOG.info(event);
    }
}
