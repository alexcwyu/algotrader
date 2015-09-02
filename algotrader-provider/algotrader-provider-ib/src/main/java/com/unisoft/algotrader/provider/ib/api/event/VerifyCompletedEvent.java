package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class VerifyCompletedEvent extends IBEvent<VerifyCompletedEvent>  {

    public final boolean isSuccessful;
    public final String errorText;

    public VerifyCompletedEvent(final boolean isSuccessful, final String errorText){
        this.isSuccessful = isSuccessful;
        this.errorText = errorText;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onVerifyCompletedEvent(this);
    }

    @Override
    public String toString() {
        return "VerifyCompletedEvent{" +
                "isSuccessful=" + isSuccessful +
                ", errorText='" + errorText + '\'' +
                "} " + super.toString();
    }
}