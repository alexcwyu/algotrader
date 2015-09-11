package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.DisplayGroupUpdatedEvent;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class DisplayGroupUpdatedEventDeserializer extends Deserializer<DisplayGroupUpdatedEvent> {


    public DisplayGroupUpdatedEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.DISPLAY_GROUP_UPDATED, serverCurrentVersion);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final int requestId = readInt(inputStream);
        final String contractInfo = readString(inputStream);

        eventHandler.onDisplayGroupUpdatedEvent(requestId, contractInfo);
    }
}