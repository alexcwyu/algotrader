package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class AccountSummaryEventDeserializer extends Deserializer {


    public AccountSummaryEventDeserializer(){
        super(IncomingMessageId.ACCOUNT_SUMMARY);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream,
                                   final IBSession ibSession) {
        final int value = readInt(inputStream);
        final String errorText = readString(inputStream);
        final String open = readString(inputStream);
        final String completedIndicator = readString(inputStream);
        final String high = readString(inputStream);
        
        ibSession.onAccountSummary(value, errorText, open, completedIndicator, high);
    }
}