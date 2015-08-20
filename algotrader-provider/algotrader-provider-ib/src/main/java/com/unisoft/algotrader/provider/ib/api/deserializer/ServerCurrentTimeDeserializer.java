package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readLong;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class ServerCurrentTimeDeserializer extends Deserializer {


    public ServerCurrentTimeDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.SERVER_CURRENT_TIME, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final long timestamp = readLong(inputStream);

        ibSession.onServerCurrentTime(timestamp);
    }
}