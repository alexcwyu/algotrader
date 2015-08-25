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


    public TickGenericEventDeserializer(){
        super(IncomingMessageId.TICK_GENERIC);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBSession ibSession) {
        final int requestId = readInt(inputStream);
        final int tickType = readInt(inputStream);
        final double value = readDouble(inputStream);

        ibSession.onTickGenericEvent(requestId, IBConstants.TickType.fromValue(tickType), value);
    }
}