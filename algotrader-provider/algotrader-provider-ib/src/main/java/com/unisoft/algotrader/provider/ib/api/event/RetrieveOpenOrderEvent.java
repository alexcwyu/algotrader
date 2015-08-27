package com.unisoft.algotrader.provider.ib.api.event;


import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.provider.ib.api.model.OrderExecution;

/**
 * Created by alex on 8/26/15.
 */
public class RetrieveOpenOrderEvent extends IBEvent<RetrieveOpenOrderEvent>  {

    public final Instrument instrument;
    public final Order order;
    public final OrderExecution orderExecution;

    public RetrieveOpenOrderEvent(final String orderId, final Instrument instrument,
                                  final Order order, final OrderExecution orderExecution){
        super(orderId);
        this.instrument = instrument;
        this.order = order;
        this.orderExecution = orderExecution;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onRetrieveOpenOrderEvent(this);
    }
}