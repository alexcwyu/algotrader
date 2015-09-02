package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.model.InstrumentSpecification;

/**
 * Created by alex on 8/26/15.
 */
public class BondInstrumentSpecificationEvent extends IBEvent<BondInstrumentSpecificationEvent>  {

    public final InstrumentSpecification contractSpecification;

    public BondInstrumentSpecificationEvent(final long requestId, final InstrumentSpecification contractSpecification){
        super(requestId);
        this.contractSpecification = contractSpecification;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onBondInstrumentSpecificationEvent(this);
    }

    @Override
    public String toString() {
        return "BondInstrumentSpecificationEvent{" +
                "contractSpecification=" + contractSpecification +
                "} " + super.toString();
    }
}