package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class DisplayGroupListEvent extends IBEvent<DisplayGroupListEvent>  {


    public final String groups;

    public DisplayGroupListEvent(final long requestId, final String groups){
        super(requestId);
        this.groups = groups;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onDisplayGroupListEvent(this);
    }
}