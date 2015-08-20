package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readDouble;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;

/**
 * Created by alex on 8/13/15.
 */
public class TickGenericEventDeserializer extends Deserializer {


    public TickGenericEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.TICK_GENERIC, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int requestId = readInt(inputStream);
        final int tickType = readInt(inputStream);
        final double value = readDouble(inputStream);

        ibSession.onTickGenericEvent(requestId, IBConstants.TickType.fromValue(tickType), value);
    }
}