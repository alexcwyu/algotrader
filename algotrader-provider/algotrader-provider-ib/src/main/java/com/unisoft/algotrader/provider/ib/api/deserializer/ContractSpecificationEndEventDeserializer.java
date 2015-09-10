package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;


/**
 * Created by alex on 8/13/15.
 */
public class ContractSpecificationEndEventDeserializer extends Deserializer {


    public ContractSpecificationEndEventDeserializer(){
        super(IncomingMessageId.CONTRACT_SPECIFICATION_END);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final int requestId = readInt(inputStream);
        eventHandler.onInstrumentSpecificationEndEvent(requestId);
    }
}