package com.unisoft.algotrader.provider.ib;

import ch.aonyx.broker.ib.api.CallbackObject;
import ch.aonyx.broker.ib.api.ClientCallback;
import ch.aonyx.broker.ib.api.NeoIbApiClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 7/27/15.
 */
public class IBClientCallback implements ClientCallback {

    private static final Logger LOG = LogManager.getLogger(IBClientCallback.class);
    @Override
    public void onSuccess(CallbackObject object) {
        LOG.info(object);
    }

    @Override
    public void onFailure(NeoIbApiClientException exception) {
        LOG.error("fail to connect",exception);
    }
}
