package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.event.ServerCurrentTimeEvent;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readLong;

/**
 * Created by alex on 8/13/15.
 */
public class ServerCurrentTimeEventDeserializer extends Deserializer<ServerCurrentTimeEvent> {


    public ServerCurrentTimeEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.SERVER_CURRENT_TIME, serverCurrentVersion);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final long timestamp = readLong(inputStream);

        eventHandler.onServerCurrentTimeEvent(timestamp);
    }
}