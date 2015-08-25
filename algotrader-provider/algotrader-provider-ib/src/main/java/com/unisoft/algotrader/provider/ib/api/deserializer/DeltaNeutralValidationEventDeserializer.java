package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readDouble;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;


/**
 * Created by alex on 8/13/15.
 */
public class DeltaNeutralValidationEventDeserializer extends Deserializer {


    public DeltaNeutralValidationEventDeserializer(){
        super(IncomingMessageId.DELTA_NEUTRAL_VALIDATION);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBSession ibSession) {
        final int requestId = readInt(inputStream);
        final int instId = (readInt(inputStream));
        final double delta = readDouble(inputStream);
        final double price = readDouble(inputStream);

        ibSession.onDeltaNeutralValidationEvent(requestId, instId, delta, price);
    }
}