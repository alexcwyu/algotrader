package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.IBConstants;

/**
 * Created by alex on 8/26/15.
 */
public class TickStringEvent extends IBEvent<TickStringEvent>  {

    public final IBConstants.TickType type;
    public final String value;

    public TickStringEvent(final String requestId, final IBConstants.TickType type,
                           final String value){
        super(requestId);
        this.type = type;
        this.value = value;
    }
}