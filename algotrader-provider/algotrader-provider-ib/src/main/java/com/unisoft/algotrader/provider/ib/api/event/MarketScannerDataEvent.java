package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.model.InstrumentSpecification;

/**
 * Created by alex on 8/26/15.
 */
public class MarketScannerDataEvent extends IBEvent<MarketScannerDataEvent>  {

    public final int ranking;
    public final InstrumentSpecification instrumentSpecification;
    public final String distance;
    public final String benchmark;
    public final String projection;
    public final String comboLegDescription;

    public MarketScannerDataEvent(final String requestId, final int ranking,
                                  final InstrumentSpecification instrumentSpecification, final String distance, final String benchmark,
                                  final String projection, final String comboLegDescription){
        super(requestId);
        this.ranking = ranking;
        this.instrumentSpecification = instrumentSpecification;
        this.distance = distance;
        this.benchmark = benchmark;
        this.projection = projection;
        this.comboLegDescription = comboLegDescription;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onMarketScannerDataEvent(this);
    }
}