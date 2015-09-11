package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.event.VerifyMessageAPIEvent;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class VerifyMessageAPIEventDeserializer extends Deserializer<VerifyMessageAPIEvent> {


    public VerifyMessageAPIEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.VERIFY_MESSAGE_API, serverCurrentVersion);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final String apiData = readString(inputStream);

        eventHandler.onVerifyMessageAPIEvent(apiData);
    }
}