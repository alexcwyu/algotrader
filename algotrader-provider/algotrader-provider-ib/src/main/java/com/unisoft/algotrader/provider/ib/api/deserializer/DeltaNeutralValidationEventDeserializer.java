package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.DeltaNeutralValidationEvent;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readDouble;
import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;


/**
 * Created by alex on 8/13/15.
 */
public class DeltaNeutralValidationEventDeserializer extends Deserializer<DeltaNeutralValidationEvent> {


    public DeltaNeutralValidationEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.DELTA_NEUTRAL_VALIDATION, serverCurrentVersion);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final int requestId = readInt(inputStream);
        final int instId = (readInt(inputStream));
        final double delta = readDouble(inputStream);
        final double price = readDouble(inputStream);

        eventHandler.onDeltaNeutralValidationEvent(requestId, instId, delta, price);
    }
}