package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;

/**
 * Created by alex on 8/13/15.
 */
public class TickSizeDeserializer extends Deserializer {

    public TickSizeDeserializer(){
        super(IncomingMessageId.TICK_SIZE);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBSession ibSession) {

        final int requestId = readInt(inputStream);
        final int tickType = readInt(inputStream);
        final int size = readInt(inputStream);

        ibSession.onTickSize(requestId, tickType, size);
    }
}