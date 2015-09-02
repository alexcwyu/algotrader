package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class RetrieveOpenOrderEndEvent extends IBEvent<RetrieveOpenOrderEndEvent>  {


    public RetrieveOpenOrderEndEvent(){
        super();
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onRetrieveOpenOrderEndEvent(this);
    }

    @Override
    public String toString() {
        return "RetrieveOpenOrderEndEvent{}";
    }
}