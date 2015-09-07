package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;

import java.io.InputStream;

/**
 * Created by alex on 8/13/15.
 */
public class PositionEndEventDeserializer extends Deserializer {


    public PositionEndEventDeserializer(){
        super(IncomingMessageId.POSITION_END);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        ibProvider.onPositionEndEvent();
    }
}