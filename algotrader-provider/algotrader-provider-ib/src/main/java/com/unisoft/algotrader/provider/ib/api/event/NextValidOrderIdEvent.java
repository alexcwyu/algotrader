package com.unisoft.algotrader.provider.ib.api.event;


/**
 * Created by alex on 8/26/15.
 */
public class NextValidOrderIdEvent extends IBEvent<NextValidOrderIdEvent>  {

    public final int nextValidOrderId;

    public NextValidOrderIdEvent(final int nextValidOrderId){
        this.nextValidOrderId = nextValidOrderId;
    }


    @Override
    public void on(IBEventHandler handler) {
        handler.onNextValidOrderIdEvent(this);
    }

    @Override
    public String toString() {
        return "NextValidOrderIdEvent{" +
                "nextValidOrderId=" + nextValidOrderId +
                "} " + super.toString();
    }
}