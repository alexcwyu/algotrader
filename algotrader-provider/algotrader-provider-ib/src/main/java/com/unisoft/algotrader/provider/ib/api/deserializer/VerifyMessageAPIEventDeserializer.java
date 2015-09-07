package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class VerifyMessageAPIEventDeserializer extends Deserializer {


    public VerifyMessageAPIEventDeserializer(){
        super(IncomingMessageId.VERIFY_MESSAGE_API);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final String apiData = readString(inputStream);

        ibProvider.onVerifyMessageAPIEvent(apiData);
    }
}