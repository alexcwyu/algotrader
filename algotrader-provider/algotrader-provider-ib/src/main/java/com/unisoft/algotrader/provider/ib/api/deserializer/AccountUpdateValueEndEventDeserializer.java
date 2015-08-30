package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;

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
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final String accountName = readString(inputStream);
        ibProvider.onAccountUpdateValueEndEvent(accountName);
    }
}