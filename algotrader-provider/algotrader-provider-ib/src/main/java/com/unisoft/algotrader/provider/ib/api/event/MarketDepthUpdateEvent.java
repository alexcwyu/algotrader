package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.IBConstants;

/**
 * Created by alex on 8/26/15.
 */
public class MarketDepthUpdateEvent extends IBEvent<MarketDepthUpdateEvent>  {

    public final int rowId;
    public final IBConstants.Operation operation;
    public final IBConstants.BookSide bookSide;
    public final double price;
    public final int size;

    public MarketDepthUpdateEvent(final String requestId, final int rowId,
                                  final IBConstants.Operation operation, final IBConstants.BookSide bookSide, final double price, final int size){
        super(requestId);
        this.rowId = rowId;
        this.operation = operation;
        this.bookSide = bookSide;
        this.price = price;
        this.size = size;
    }

}