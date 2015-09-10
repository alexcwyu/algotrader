package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class VerifyCompletedEventDeserializer extends Deserializer {


    public VerifyCompletedEventDeserializer(){
        super(IncomingMessageId.VERIFY_COMPLETED);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final boolean isSuccessful = "true".equals(readString(inputStream));

        final String errorText = readString(inputStream);

        //TODO
        //if(isSuccessful) {
        // this.m_parent.startAPI();
        //}
        eventHandler.onVerifyCompletedEvent(isSuccessful, errorText);
    }
}