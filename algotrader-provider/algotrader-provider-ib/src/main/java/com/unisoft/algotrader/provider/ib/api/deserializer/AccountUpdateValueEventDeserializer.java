package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class AccountUpdateValueEventDeserializer extends Deserializer {


    public AccountUpdateValueEventDeserializer(){
        super(IncomingMessageId.ACCOUNT_UPDATE_VALUE);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final String key = readString(inputStream);
        final String value = readString(inputStream);
        final String currency = readString(inputStream);
        final String accountName = (version >= 2) ? readString(inputStream) : null;

        ibProvider.onAccountUpdateValueEvent(key, value, currency, accountName);

    }
}