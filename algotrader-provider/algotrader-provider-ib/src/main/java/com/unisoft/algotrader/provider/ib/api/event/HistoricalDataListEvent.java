package com.unisoft.algotrader.provider.ib.api.event;


import java.util.List;

/**
 * Created by alex on 8/26/15.
 */
public class HistoricalDataListEvent extends IBEvent<HistoricalDataListEvent>  {

    public final List<HistoricalDataEvent> historicalDataEvents;
    public HistoricalDataListEvent(final String requestId, List<HistoricalDataEvent> historicalDataEvents){
        super(requestId);
        this.historicalDataEvents = historicalDataEvents;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onHistoricalDataListEvent(this);
    }
}