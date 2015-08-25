package com.unisoft.algotrader.provider.ib.api.event;


/**
 * Created by alex on 8/26/15.
 */
public class MarketScannerValidParametersEvent extends IBEvent<MarketScannerValidParametersEvent>  {

    public final String xml;
    public MarketScannerValidParametersEvent(final String xml){
        this.xml = xml;
    }
}