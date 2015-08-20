package com.unisoft.algotrader.provider.ib.api.deserializer;

import ch.aonyx.broker.ib.api.util.InputStreamUtils;
import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class TickStringEventDeserializer extends Deserializer {


    public TickStringEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.TICK_STRING, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int requestId = InputStreamUtils.readInt(inputStream);
        final int tickType = InputStreamUtils.readInt(inputStream);
        final String value = readString(inputStream);
        
        ibSession.onTickStringEvent(requestId, IBConstants.TickType.fromValue(tickType), value);
    }
}