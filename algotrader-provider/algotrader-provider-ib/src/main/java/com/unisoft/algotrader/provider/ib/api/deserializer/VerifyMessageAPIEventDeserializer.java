package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class VerifyMessageAPIEventDeserializer extends Deserializer {


    public VerifyMessageAPIEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.VERIFY_MESSAGE_API, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final String message = readString(inputStream);
        
        ibSession.onVerifyMessageAPI(message);
    }
}