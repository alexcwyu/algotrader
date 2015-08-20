package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class VerifyCompletedEventDeserializer extends Deserializer {


    public VerifyCompletedEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.VERIFY_COMPLETED, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final boolean bool = "true".equals(readString(inputStream));

        final String errorText = readString(inputStream);
        
        ibSession.onVerifyCompleted(bool, errorText);
    }
}