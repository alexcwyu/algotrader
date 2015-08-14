package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class ServerMessageDeserializer extends Deserializer {


    public ServerMessageDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.SERVER_MESSAGE, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        if (getVersion() < VERSION_2) {
            final String message = readString(inputStream);
            ibSession.onMessage(-1, 0, message);
        } else {
            final int requestId = readInt(inputStream);
            final int code = readInt(inputStream);
            final String message = readString(inputStream);
            ibSession.onMessage(requestId, code, message);
        }
    }
}