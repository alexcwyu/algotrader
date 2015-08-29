package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBSocket;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;
import com.unisoft.algotrader.provider.ib.api.InputStreamUtils;

import java.io.InputStream;

/**
 * Created by alex on 8/13/15.
 */
public class NextValidOrderIdEventDeserializer extends Deserializer {


    public NextValidOrderIdEventDeserializer(){
        super(IncomingMessageId.NEXT_VALID_ORDER_ID);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final int nextValidOrderId = InputStreamUtils.readInt(inputStream);
        ibProvider.onNextValidOrderIdEvent(nextValidOrderId);

    }
}