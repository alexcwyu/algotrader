package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.model.constants.BookSide;
import com.unisoft.algotrader.provider.ib.api.model.constants.Operation;

/**
 * Created by alex on 8/26/15.
 */
public class MarketDepthLevelTwoUpdateEvent extends IBEvent<MarketDepthLevelTwoUpdateEvent>  {

    public final int rowId;
    public final String marketMakerName;
    public final Operation operation;
    public final BookSide bookSide;
    public final double price;
    public final int size;

    public MarketDepthLevelTwoUpdateEvent(final long requestId, final int rowId, final String marketMakerName,
                                          final Operation operation, final BookSide bookSide, final double price, final int size){
        super(requestId);
        this.rowId = rowId;
        this.marketMakerName = marketMakerName;
        this.operation = operation;
        this.bookSide = bookSide;
        this.price = price;
        this.size = size;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onMarketDepthLevelTwoUpdateEvent(this);
    }

}