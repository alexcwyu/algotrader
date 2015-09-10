package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.InputStreamUtils;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

/**
 * Created by alex on 8/13/15.
 */
public class NextValidOrderIdEventDeserializer extends Deserializer {


    public NextValidOrderIdEventDeserializer(){
        super(IncomingMessageId.NEXT_VALID_ORDER_ID);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final int nextValidOrderId = InputStreamUtils.readInt(inputStream);
        eventHandler.onNextValidOrderIdEvent(nextValidOrderId);

    }
}