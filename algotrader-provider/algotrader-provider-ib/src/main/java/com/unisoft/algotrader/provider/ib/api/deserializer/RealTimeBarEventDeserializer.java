package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class RealTimeBarEventDeserializer extends Deserializer {


    public RealTimeBarEventDeserializer(){
        super(IncomingMessageId.REAL_TIME_BAR);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final int requestId = readInt(inputStream);
        final long timestamp = readLong(inputStream);
        final double open = readDouble(inputStream);
        final double high = readDouble(inputStream);
        final double low = readDouble(inputStream);
        final double close = readDouble(inputStream);
        final long volume = readLong(inputStream);
        final double weightedAveragePrice = readDouble(inputStream);
        final int tradeNumber = readInt(inputStream);
        ibProvider.onRealTimeBarEvent(requestId, timestamp, open,
        high, low, close, volume, weightedAveragePrice,  tradeNumber);
    }
}