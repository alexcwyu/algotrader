package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBSocket;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;

/**
 * Created by alex on 8/13/15.
 */
public class AccountSummaryEndEventDeserializer extends Deserializer {


    public AccountSummaryEndEventDeserializer(){
        super(IncomingMessageId.ACCOUNT_SUMMARY_END);
    }

    @Override
    public void consumeVersionLess(final int version, InputStream inputStream, IBProvider ibProvider) {
        final int reqId = readInt(inputStream);

        ibProvider.onAccountSummaryEndEvent(reqId);
    }
}