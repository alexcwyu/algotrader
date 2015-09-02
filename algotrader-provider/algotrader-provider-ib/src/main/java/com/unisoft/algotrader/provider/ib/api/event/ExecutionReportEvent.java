package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.model.event.execution.ExecutionReport;
import com.unisoft.algotrader.model.refdata.Instrument;

/**
 * Created by alex on 8/26/15.
 */
public class ExecutionReportEvent extends IBEvent<ExecutionReportEvent>  {

    public final Instrument instrument;
    public final ExecutionReport executionReport;

    public ExecutionReportEvent(final long requestId, final Instrument instrument, final ExecutionReport executionReport){
        super(requestId);
        this.instrument = instrument;
        this.executionReport = executionReport;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onExecutionReportEvent(this);
    }

    @Override
    public String toString() {
        return "ExecutionReportEvent{" +
                "instrument=" + instrument +
                ", executionReport=" + executionReport +
                "} " + super.toString();
    }
}