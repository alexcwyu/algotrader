package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class AccountUpdateEventDeserializer extends Deserializer {


    public AccountUpdateEventDeserializer(){
        super(IncomingMessageId.ACCOUNT_UPDATE_VALUE);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBSession ibSession) {
        final String key = readString(inputStream);
        final String value = readString(inputStream);
        final String currency = readString(inputStream);
        final String accountName = (version >= 2) ? readString(inputStream) : null;

        ibSession.onUpdateAccountValue(key, value, currency, accountName);

    }
}