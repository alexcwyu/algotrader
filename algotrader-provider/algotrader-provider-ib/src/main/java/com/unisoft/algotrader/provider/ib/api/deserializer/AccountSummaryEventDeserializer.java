package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.AccountSummaryEvent;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class AccountSummaryEventDeserializer extends Deserializer<AccountSummaryEvent> {


    public AccountSummaryEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.ACCOUNT_SUMMARY, serverCurrentVersion);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final int reqId = readInt(inputStream);
        final String account = readString(inputStream);
        final String tag = readString(inputStream);
        final String value = readString(inputStream);
        final String currency = readString(inputStream);

        eventHandler.onAccountSummaryEvent(reqId, account, tag, value, currency);
    }
}