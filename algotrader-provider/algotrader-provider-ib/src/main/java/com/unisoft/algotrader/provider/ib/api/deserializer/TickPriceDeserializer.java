package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readDouble;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;

/**
 * Created by alex on 8/13/15.
 */
public class TickPriceDeserializer extends Deserializer {


    public TickPriceDeserializer(){
        super(IncomingMessageId.TICK_PRICE);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBSession ibSession) {

        final int requestId = readInt(inputStream);
        final int tickType = readInt(inputStream);
        final double price = readDouble(inputStream);

        final int size = (version >= VERSION_2) ? readInt(inputStream): 0;
        final int autoExecute = (version >= VERSION_3) ? readInt(inputStream) : 0;

        ibSession.onTickPrice(requestId, tickType, price, autoExecute);
        ibSession.onTickSize(requestId, tickType, size);
    }
}