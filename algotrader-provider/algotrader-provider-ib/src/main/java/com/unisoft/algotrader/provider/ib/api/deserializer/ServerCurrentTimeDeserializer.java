package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readLong;

/**
 * Created by alex on 8/13/15.
 */
public class ServerCurrentTimeDeserializer extends Deserializer {


    public ServerCurrentTimeDeserializer(){
        super(IncomingMessageId.SERVER_CURRENT_TIME);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBSession ibSession) {
        final long timestamp = readLong(inputStream);

        ibSession.onServerCurrentTime(timestamp);
    }
}