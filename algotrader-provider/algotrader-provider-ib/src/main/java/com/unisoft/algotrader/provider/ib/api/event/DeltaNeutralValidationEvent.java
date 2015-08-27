package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.model.UnderlyingCombo;

/**
 * Created by alex on 8/26/15.
 */
public class DeltaNeutralValidationEvent extends IBEvent<DeltaNeutralValidationEvent>  {

    public final UnderlyingCombo underlyingCombo;

    public DeltaNeutralValidationEvent(final String requestId, final UnderlyingCombo underlyingCombo){
        super(requestId);
        this.underlyingCombo = underlyingCombo;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onDeltaNeutralValidationEvent(this);
    }
}