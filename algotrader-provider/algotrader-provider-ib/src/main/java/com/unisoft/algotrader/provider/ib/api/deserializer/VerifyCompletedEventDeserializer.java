package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBSocket;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class VerifyCompletedEventDeserializer extends Deserializer {


    public VerifyCompletedEventDeserializer(){
        super(IncomingMessageId.VERIFY_COMPLETED);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final boolean isSuccessful = "true".equals(readString(inputStream));

        final String errorText = readString(inputStream);

        ibProvider.onVerifyCompletedEvent(isSuccessful, errorText);
    }
}