package com.unisoft.algotrader.provider.ib.neoib.listener;

import ch.aonyx.broker.ib.api.contract.ContractSpecificationEvent;
import ch.aonyx.broker.ib.api.contract.ContractSpecificationEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/24/15.
 */
public class MyContractSpecificationEventListener implements ContractSpecificationEventListener {

    private static final Logger LOG = LogManager.getLogger(MyContractSpecificationEventListener.class);
    @Override
    public void notify(ContractSpecificationEvent event) {
        LOG.info(event);
    }
}
