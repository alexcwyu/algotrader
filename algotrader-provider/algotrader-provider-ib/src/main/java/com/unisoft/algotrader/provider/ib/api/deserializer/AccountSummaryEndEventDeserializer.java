package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.AccountSummaryEndEvent;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;

/**
 * Created by alex on 8/13/15.
 */
public class AccountSummaryEndEventDeserializer extends Deserializer<AccountSummaryEndEvent> {


    public AccountSummaryEndEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.ACCOUNT_SUMMARY_END, serverCurrentVersion);
    }

    @Override
    public void consumeMessageContent(final int version, InputStream inputStream, IBEventHandler eventHandler) {
        final int reqId = readInt(inputStream);

        eventHandler.onAccountSummaryEndEvent(reqId);
    }
}