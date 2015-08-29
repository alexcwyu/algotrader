package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBSocket;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class ServerMessageDeserializer extends Deserializer {


    public ServerMessageDeserializer(){
        super(IncomingMessageId.SERVER_MESSAGE);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        if (version < VERSION_2) {
            final String message = readString(inputStream);
            ibProvider.onServerMessageEvent(-1, 0, message);
        } else {
            final int requestId = readInt(inputStream);
            final int code = readInt(inputStream);
            final String message = readString(inputStream);
            ibProvider.onServerMessageEvent(requestId, code, message);
        }
    }
}