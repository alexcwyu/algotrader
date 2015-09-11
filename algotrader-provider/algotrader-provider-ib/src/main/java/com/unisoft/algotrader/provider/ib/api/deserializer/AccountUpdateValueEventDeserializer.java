package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.AccountUpdateValueEvent;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class AccountUpdateValueEventDeserializer extends Deserializer<AccountUpdateValueEvent> {


    public AccountUpdateValueEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.ACCOUNT_UPDATE_VALUE, serverCurrentVersion);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final String key = readString(inputStream);
        final String value = readString(inputStream);
        final String currency = readString(inputStream);
        final String accountName = (version >= 2) ? readString(inputStream) : null;

        eventHandler.onAccountUpdateValueEvent(key, value, currency, accountName);

    }
}