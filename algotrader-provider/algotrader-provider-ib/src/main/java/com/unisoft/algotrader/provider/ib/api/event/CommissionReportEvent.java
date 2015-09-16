package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.model.execution.IBCommissionReport;

/**
 * Created by alex on 8/26/15.
 */
public class CommissionReportEvent extends IBEvent<CommissionReportEvent>  {

    public final IBCommissionReport IBCommissionReport;

    public CommissionReportEvent(final IBCommissionReport IBCommissionReport){
        this.IBCommissionReport = IBCommissionReport;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onCommissionReportEvent(this);
    }

    @Override
    public String toString() {
        return "CommissionReportEvent{" +
                "commissionReport=" + IBCommissionReport +
                "} " + super.toString();
    }
}