package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class AccountSummaryEventDeserializer extends Deserializer {


    public AccountSummaryEventDeserializer(){
        super(IncomingMessageId.ACCOUNT_SUMMARY);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream,
                                      final IBProvider ibProvider) {
        final int reqId = readInt(inputStream);
        final String account = readString(inputStream);
        final String tag = readString(inputStream);
        final String value = readString(inputStream);
        final String currency = readString(inputStream);

        ibProvider.onAccountSummaryEvent(reqId, account, tag, value, currency);
    }
}