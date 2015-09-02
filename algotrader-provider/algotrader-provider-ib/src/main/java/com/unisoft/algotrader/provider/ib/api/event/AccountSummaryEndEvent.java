package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class AccountSummaryEndEvent extends IBEvent<AccountSummaryEndEvent>  {

    public final int reqId;

    public AccountSummaryEndEvent(final int reqId){
        this.reqId = reqId;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onAccountSummaryEndEvent(this);
    }

    @Override
    public String toString() {
        return "AccountSummaryEndEvent{" +
                "reqId=" + reqId +
                "} " + super.toString();
    }
}