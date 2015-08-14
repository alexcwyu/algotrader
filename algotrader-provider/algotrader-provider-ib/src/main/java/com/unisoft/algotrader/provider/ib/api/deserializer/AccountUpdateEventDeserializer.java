package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class AccountUpdateEventDeserializer extends Deserializer {


    public AccountUpdateEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.ACCOUNT_UPDATE_VALUE,serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final String key = readString(inputStream);
        final String value = readString(inputStream);
        final String currency = readString(inputStream);
        final String accountName = (getVersion() >= 2) ? readString(inputStream) : null;

        ibSession.onUpdateAccountValue(key, value, currency, accountName);

    }
}