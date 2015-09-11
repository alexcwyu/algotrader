package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.AccountUpdateTimeEvent;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class AccountUpdateTimeEventDeserializer extends Deserializer<AccountUpdateTimeEvent> {


    public AccountUpdateTimeEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.ACCOUNT_UPDATE_TIME, serverCurrentVersion);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final String time = readString(inputStream);
        eventHandler.onAccountUpdateTimeEvent(time);

    }
}