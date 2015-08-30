package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;

import java.io.InputStream;


/**
 * Created by alex on 8/13/15.
 */
public class RetrieveOpenOrderEndEventDeserializer extends Deserializer {


    public RetrieveOpenOrderEndEventDeserializer(){
        super(IncomingMessageId.RETRIEVE_OPEN_ORDER_END);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        //TODO
        ibProvider.onRetrieveOpenOrderEndEvent();
    }
}