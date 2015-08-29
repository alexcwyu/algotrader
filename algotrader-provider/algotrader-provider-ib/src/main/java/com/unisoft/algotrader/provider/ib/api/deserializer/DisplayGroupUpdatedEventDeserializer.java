package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBSocket;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class DisplayGroupUpdatedEventDeserializer extends Deserializer {


    public DisplayGroupUpdatedEventDeserializer(){
        super(IncomingMessageId.DISPLAY_GROUP_UPDATED);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final int requestId = readInt(inputStream);
        final String contractInfo = readString(inputStream);

        ibProvider.onDisplayGroupUpdatedEvent(requestId, contractInfo);
    }
}