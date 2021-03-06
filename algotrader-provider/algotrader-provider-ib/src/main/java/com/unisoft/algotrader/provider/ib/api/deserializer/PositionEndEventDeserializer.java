package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.event.PositionEndEvent;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

/**
 * Created by alex on 8/13/15.
 */
public class PositionEndEventDeserializer extends Deserializer<PositionEndEvent> {


    public PositionEndEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.POSITION_END, serverCurrentVersion);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        eventHandler.onPositionEndEvent();
    }
}