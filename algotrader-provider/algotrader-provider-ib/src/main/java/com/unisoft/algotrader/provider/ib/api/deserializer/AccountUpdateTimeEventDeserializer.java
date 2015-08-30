package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class AccountUpdateTimeEventDeserializer extends Deserializer {


    public AccountUpdateTimeEventDeserializer(){
        super(IncomingMessageId.ACCOUNT_UPDATE_TIME);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final String time = readString(inputStream);
        ibProvider.onAccountUpdateTimeEvent(time);

    }
}