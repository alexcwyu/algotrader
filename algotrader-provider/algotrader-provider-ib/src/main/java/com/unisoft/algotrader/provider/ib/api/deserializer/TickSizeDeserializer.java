package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;
import com.unisoft.algotrader.provider.ib.api.model.constants.TickType;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;

/**
 * Created by alex on 8/13/15.
 */
public class TickSizeDeserializer extends Deserializer {

    public TickSizeDeserializer(){
        super(IncomingMessageId.TICK_SIZE);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {

        final int requestId = readInt(inputStream);
        final int tickType = readInt(inputStream);
        final int size = readInt(inputStream);

        ibProvider.onTickSizeEvent(requestId, TickType.fromValue(tickType), size);
    }
}