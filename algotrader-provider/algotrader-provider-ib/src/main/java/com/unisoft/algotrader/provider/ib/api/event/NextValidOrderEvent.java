package com.unisoft.algotrader.provider.ib.api.event;


/**
 * Created by alex on 8/26/15.
 */
public class NextValidOrderEvent extends IBEvent<NextValidOrderEvent>  {

    public final int nextValidOrderId;

    public NextValidOrderEvent(final int nextValidOrderId){
        this.nextValidOrderId = nextValidOrderId;
    }


    @Override
    public void on(IBEventHandler handler) {
        handler.onNextValidOrderEvent(this);
    }
}