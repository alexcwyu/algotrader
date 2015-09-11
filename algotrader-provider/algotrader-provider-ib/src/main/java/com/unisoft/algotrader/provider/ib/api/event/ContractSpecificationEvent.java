package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.model.contract.ContractSpecification;

/**
 * Created by alex on 8/26/15.
 */
public class ContractSpecificationEvent extends IBEvent<ContractSpecificationEvent>  {

    public final ContractSpecification contractSpecification;

    public ContractSpecificationEvent(final long requestId, final ContractSpecification contractSpecification){
        super(requestId);
        this.contractSpecification = contractSpecification;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onInstrumentSpecificationEvent(this);
    }

    @Override
    public String toString() {
        return "ContractSpecificationEvent{" +
                "contractSpecification=" + contractSpecification +
                "} " + super.toString();
    }
}