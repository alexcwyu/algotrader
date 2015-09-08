package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.data.TickType;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readDouble;
import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;

/**
 * Created by alex on 8/13/15.
 */
public class TickGenericEventDeserializer extends Deserializer {


    public TickGenericEventDeserializer(){
        super(IncomingMessageId.TICK_GENERIC);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final int requestId = readInt(inputStream);
        final int tickType = readInt(inputStream);
        final double value = readDouble(inputStream);

        ibProvider.onTickGenericEvent(requestId, TickType.fromValue(tickType), value);
    }
}