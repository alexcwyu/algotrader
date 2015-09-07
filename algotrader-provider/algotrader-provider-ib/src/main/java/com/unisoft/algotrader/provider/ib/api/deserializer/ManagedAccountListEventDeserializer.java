package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class ManagedAccountListEventDeserializer extends Deserializer {


    public ManagedAccountListEventDeserializer(){
        super(IncomingMessageId.MANAGED_ACCOUNT_LIST);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final String commaSeparatedAccountList = readString(inputStream);
        ibProvider.onManagedAccountListEvent(commaSeparatedAccountList);
    }
}