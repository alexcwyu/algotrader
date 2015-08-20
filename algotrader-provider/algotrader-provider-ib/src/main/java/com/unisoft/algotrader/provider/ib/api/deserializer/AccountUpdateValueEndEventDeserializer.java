package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static ch.aonyx.broker.ib.api.util.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class AccountUpdateValueEndEventDeserializer extends Deserializer {


    public AccountUpdateValueEndEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.ACCOUNT_UPDATE_VALUE_END, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final String accountName = readString(inputStream);
        ibSession.onAccountUpdateValueEndEvent(accountName);
    }
}