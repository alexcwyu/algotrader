package com.unisoft.algotrader.provider.ib.neoib.listener;

import ch.aonyx.broker.ib.api.execution.ExecutionReportEndEvent;
import ch.aonyx.broker.ib.api.execution.ExecutionReportEndEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/24/15.
 */
public class MyExecutionReportEndEventListener implements ExecutionReportEndEventListener {
    private static final Logger LOG = LogManager.getLogger(MyExecutionReportEndEventListener.class);

    @Override
    public void notify(ExecutionReportEndEvent event) {
        LOG.info(event);
    }
}
