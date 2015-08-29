package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class TickSnapshotEndEvent extends IBEvent<TickSnapshotEndEvent>  {


    public TickSnapshotEndEvent(final long requestId){
        super(requestId);
    }


    @Override
    public void on(IBEventHandler handler) {
        handler.onTickSnapshotEndEvent(this);
    }
}