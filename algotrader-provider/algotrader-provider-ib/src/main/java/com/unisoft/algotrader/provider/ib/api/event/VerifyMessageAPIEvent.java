package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class VerifyMessageAPIEvent extends IBEvent<VerifyMessageAPIEvent>  {

    public final String apiData;

    public VerifyMessageAPIEvent(final String apiData){
        this.apiData = apiData;
    }


    @Override
    public void on(IBEventHandler handler) {
        handler.onVerifyMessageAPIEvent(this);
    }

    @Override
    public String toString() {
        return "VerifyMessageAPIEvent{" +
                "apiData='" + apiData + '\'' +
                "} " + super.toString();
    }
}