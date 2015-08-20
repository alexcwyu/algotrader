package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class AccountSummaryEndEventDeserializer extends Deserializer {


    public AccountSummaryEndEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.ACCOUNT_SUMMARY_END, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int value = readInt(inputStream);
        
        ibSession.onAccountSummaryEnd(value);
    }
}