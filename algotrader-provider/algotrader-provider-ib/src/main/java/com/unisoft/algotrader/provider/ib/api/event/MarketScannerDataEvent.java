package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.model.contract.ContractSpecification;

/**
 * Created by alex on 8/26/15.
 */
public class MarketScannerDataEvent extends IBEvent<MarketScannerDataEvent>  {

    public final int ranking;
    public final ContractSpecification contractSpecification;
    public final String distance;
    public final String benchmark;
    public final String projection;
    public final String comboLegDescription;

    public MarketScannerDataEvent(final long requestId, final int ranking,
                                  final ContractSpecification contractSpecification, final String distance, final String benchmark,
                                  final String projection, final String comboLegDescription){
        super(requestId);
        this.ranking = ranking;
        this.contractSpecification = contractSpecification;
        this.distance = distance;
        this.benchmark = benchmark;
        this.projection = projection;
        this.comboLegDescription = comboLegDescription;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onMarketScannerDataEvent(this);
    }

    @Override
    public String toString() {
        return "MarketScannerDataEvent{" +
                "ranking=" + ranking +
                ", instrumentSpecification=" + contractSpecification +
                ", distance='" + distance + '\'' +
                ", benchmark='" + benchmark + '\'' +
                ", projection='" + projection + '\'' +
                ", comboLegDescription='" + comboLegDescription + '\'' +
                "} " + super.toString();
    }
}