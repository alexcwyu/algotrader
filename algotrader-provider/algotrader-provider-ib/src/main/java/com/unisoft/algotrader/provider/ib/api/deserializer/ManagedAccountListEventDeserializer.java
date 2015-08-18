package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class ManagedAccountListEventDeserializer extends Deserializer {


    public ManagedAccountListEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.MANAGED_ACCOUNT_LIST, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final String commaSeparatedAccountList = readString(inputStream);
        ibSession.onManagedAccountList(commaSeparatedAccountList);
    }
}