package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;


/**
 * Created by alex on 8/13/15.
 */
public class DeltaNeutralValidationEventDeserializer extends Deserializer {


    public DeltaNeutralValidationEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.DELTA_NEUTRAL_VALIDATION, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int requestId = readInt(inputStream);
        final int instId = (readInt(inputStream));
        final double delta = readDouble(inputStream);
        final double price = readDouble(inputStream);

        ibSession.onDeltaNeutralValidationEvent(requestId, instId, delta, price);
    }
}