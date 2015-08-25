package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.IBConstants;

/**
 * Created by alex on 8/26/15.
 */
public class TickGenericEvent extends IBEvent<TickGenericEvent>  {

    public final IBConstants.TickType type;
    public final double value;

    public TickGenericEvent(final String requestId, final IBConstants.TickType type, final double value){
        super(requestId);
        this.type = type;
        this.value = value;
    }
}
