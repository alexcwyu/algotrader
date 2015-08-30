package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readDouble;
import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;


/**
 * Created by alex on 8/13/15.
 */
public class DeltaNeutralValidationEventDeserializer extends Deserializer {


    public DeltaNeutralValidationEventDeserializer(){
        super(IncomingMessageId.DELTA_NEUTRAL_VALIDATION);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final int requestId = readInt(inputStream);
        final int instId = (readInt(inputStream));
        final double delta = readDouble(inputStream);
        final double price = readDouble(inputStream);

        ibProvider.onDeltaNeutralValidationEvent(requestId, instId, delta, price);
    }
}