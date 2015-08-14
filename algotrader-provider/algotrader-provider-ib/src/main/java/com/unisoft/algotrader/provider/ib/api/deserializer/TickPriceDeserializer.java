package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class TickPriceDeserializer extends Deserializer {


    public TickPriceDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.TICK_PRICE, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {

        final int requestId = readInt(inputStream);
        final int tickType = readInt(inputStream);
        final double price = readDouble(inputStream);

        final int size = (getVersion() >= VERSION_2) ? readInt(inputStream): 0;
        final int autoExecute = (getVersion() >= VERSION_3) ? readInt(inputStream) : 0;

        ibSession.onTickPrice(requestId, tickType, price, autoExecute);
        ibSession.onTickSize(requestId, tickType, size);
    }
}