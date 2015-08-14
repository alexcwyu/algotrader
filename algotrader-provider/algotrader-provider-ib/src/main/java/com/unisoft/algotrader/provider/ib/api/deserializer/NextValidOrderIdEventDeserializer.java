package com.unisoft.algotrader.provider.ib.api.deserializer;

import ch.aonyx.broker.ib.api.util.InputStreamUtils;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class NextValidOrderIdEventDeserializer extends Deserializer {


    public NextValidOrderIdEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.NEXT_VALID_ORDER_ID, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int nextValidOrderId = InputStreamUtils.readInt(inputStream);
        ibSession.onNextValidOrderId(nextValidOrderId);

    }
}