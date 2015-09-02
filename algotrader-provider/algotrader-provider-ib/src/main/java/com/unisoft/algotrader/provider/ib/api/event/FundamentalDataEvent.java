package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class FundamentalDataEvent extends IBEvent<FundamentalDataEvent>  {

    public final String xml;
    public FundamentalDataEvent(final long requestId, final String xml){
        super(requestId);
        this.xml = xml;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onFundamentalDataEvent(this);
    }

    @Override
    public String toString() {
        return "FundamentalDataEvent{" +
                "xml='" + xml + '\'' +
                "} " + super.toString();
    }
}