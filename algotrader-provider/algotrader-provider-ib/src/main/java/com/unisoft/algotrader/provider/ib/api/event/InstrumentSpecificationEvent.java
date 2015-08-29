package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.model.InstrumentSpecification;

/**
 * Created by alex on 8/26/15.
 */
public class InstrumentSpecificationEvent extends IBEvent<InstrumentSpecificationEvent>  {

    public final InstrumentSpecification instrumentSpecification;

    public InstrumentSpecificationEvent(final long requestId, final InstrumentSpecification instrumentSpecification){
        super(requestId);
        this.instrumentSpecification = instrumentSpecification;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onInstrumentSpecificationEvent(this);
    }
}