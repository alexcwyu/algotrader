package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class FundamentalDataEvent extends IBEvent<FundamentalDataEvent>  {

    public final String xml;
    public FundamentalDataEvent(final String requestId, final String xml){
        super(requestId);
        this.xml = xml;
    }
}