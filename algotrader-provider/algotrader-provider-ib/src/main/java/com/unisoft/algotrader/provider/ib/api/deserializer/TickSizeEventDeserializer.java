package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.event.TickSizeEvent;
import com.unisoft.algotrader.provider.ib.api.model.data.TickType;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;

/**
 * Created by alex on 8/13/15.
 */
public class TickSizeEventDeserializer extends Deserializer<TickSizeEvent> {

    public TickSizeEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.TICK_SIZE, serverCurrentVersion);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {

        final int requestId = readInt(inputStream);
        final int tickType = readInt(inputStream);
        final int size = readInt(inputStream);

        eventHandler.onTickSizeEvent(requestId, TickType.fromValue(tickType), size);
    }
}