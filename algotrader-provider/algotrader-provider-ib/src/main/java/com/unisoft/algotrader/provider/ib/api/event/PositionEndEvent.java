package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class PositionEndEvent extends IBEvent<PositionEndEvent>  {

    public PositionEndEvent(){
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onPositionEndEvent(this);
    }

    @Override
    public String toString() {
        return "PositionEndEvent{}";
    }
}