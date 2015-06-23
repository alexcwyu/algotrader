package com.unisoft.algotrader.provider.ib.neoib.listener;

import ch.aonyx.broker.ib.api.execution.ExecutionReportEvent;
import ch.aonyx.broker.ib.api.execution.ExecutionReportEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/24/15.
 */
public class MyExecutionReportEventListener implements ExecutionReportEventListener {

    private static final Logger LOG = LogManager.getLogger(MyExecutionReportEventListener.class);
    @Override
    public void notify(ExecutionReportEvent event) {
        LOG.info(event);
    }
}
