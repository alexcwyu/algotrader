package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class DisplayGroupUpdatedEvent extends IBEvent<DisplayGroupUpdatedEvent>  {

    public final String contractInfo;

    public DisplayGroupUpdatedEvent(final long requestId, final String contractInfo){
        super(requestId);
        this.contractInfo = contractInfo;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onDisplayGroupUpdatedEvent(this);
    }
}