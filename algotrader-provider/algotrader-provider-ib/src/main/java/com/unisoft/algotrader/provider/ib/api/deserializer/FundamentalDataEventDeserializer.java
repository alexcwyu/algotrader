package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;


/**
 * Created by alex on 8/13/15.
 */
public class FundamentalDataEventDeserializer extends Deserializer {


    public FundamentalDataEventDeserializer(){
        super(IncomingMessageId.FUNDAMENTAL_DATA);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final int requestId = readInt(inputStream);
        final String xml = readString(inputStream);
        ibProvider.onFundamentalDataEvent(requestId, xml);
    }
}