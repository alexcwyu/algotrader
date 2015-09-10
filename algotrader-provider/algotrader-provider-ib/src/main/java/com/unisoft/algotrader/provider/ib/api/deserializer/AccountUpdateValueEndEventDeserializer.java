package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class AccountUpdateValueEndEventDeserializer extends Deserializer {


    public AccountUpdateValueEndEventDeserializer(){
        super(IncomingMessageId.ACCOUNT_UPDATE_VALUE_END);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final String accountName = readString(inputStream);
        eventHandler.onAccountUpdateValueEndEvent(accountName);
    }
}