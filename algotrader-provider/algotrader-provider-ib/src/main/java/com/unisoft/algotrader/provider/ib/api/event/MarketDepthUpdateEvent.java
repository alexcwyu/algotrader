package com.unisoft.algotrader.provider.ib.api.event;

import com.unisoft.algotrader.provider.ib.api.model.data.BookSide;
import com.unisoft.algotrader.provider.ib.api.model.data.Operation;

/**
 * Created by alex on 8/26/15.
 */
public class MarketDepthUpdateEvent extends IBEvent<MarketDepthUpdateEvent>  {

    public final int rowId;
    public final Operation operation;
    public final BookSide bookSide;
    public final double price;
    public final int size;

    public MarketDepthUpdateEvent(final long requestId, final int rowId,
                                  final Operation operation, final BookSide bookSide, final double price, final int size){
        super(requestId);
        this.rowId = rowId;
        this.operation = operation;
        this.bookSide = bookSide;
        this.price = price;
        this.size = size;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onMarketDepthUpdateEvent(this);
    }

    @Override
    public String toString() {
        return "MarketDepthUpdateEvent{" +
                "rowId=" + rowId +
                ", operation=" + operation +
                ", bookSide=" + bookSide +
                ", price=" + price +
                ", size=" + size +
                "} " + super.toString();
    }
}