package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;

/**
 * Created by alex on 8/13/15.
 */
public class TickSnapshotEndEventDeserializer extends Deserializer {


    public TickSnapshotEndEventDeserializer(){
        super(IncomingMessageId.TICK_SNAPSHOT_END);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final int requestId = readInt(inputStream);

        ibProvider.onTickSnapshotEndEvent(requestId);
    }
}