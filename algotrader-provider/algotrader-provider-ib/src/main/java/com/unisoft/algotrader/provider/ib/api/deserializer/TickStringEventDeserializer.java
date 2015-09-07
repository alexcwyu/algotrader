package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.InputStreamUtils;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;
import com.unisoft.algotrader.provider.ib.api.model.constants.TickType;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class TickStringEventDeserializer extends Deserializer {


    public TickStringEventDeserializer(){
        super(IncomingMessageId.TICK_STRING);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final int requestId = InputStreamUtils.readInt(inputStream);
        final int tickType = InputStreamUtils.readInt(inputStream);
        final String value = readString(inputStream);

        ibProvider.onTickStringEvent(requestId, TickType.fromValue(tickType), value);
    }
}