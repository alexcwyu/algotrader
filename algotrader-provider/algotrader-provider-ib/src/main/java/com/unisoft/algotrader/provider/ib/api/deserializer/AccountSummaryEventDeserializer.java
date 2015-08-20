package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class AccountSummaryEventDeserializer extends Deserializer {


    public AccountSummaryEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.ACCOUNT_SUMMARY, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int value = readInt(inputStream);
        final String errorText = readString(inputStream);
        final String open = readString(inputStream);
        final String completedIndicator = readString(inputStream);
        final String high = readString(inputStream);
        
        ibSession.onAccountSummary(value, errorText, open, completedIndicator, high);
    }
}