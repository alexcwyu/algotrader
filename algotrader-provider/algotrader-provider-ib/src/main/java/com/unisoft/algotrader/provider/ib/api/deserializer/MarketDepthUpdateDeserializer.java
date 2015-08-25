package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readDouble;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;

/**
 * Created by alex on 8/13/15.
 */
public class MarketDepthUpdateDeserializer extends Deserializer {


    public MarketDepthUpdateDeserializer(){
        super(IncomingMessageId.MARKET_DEPTH_UPDATE);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBSession ibSession) {
        final int requestId = readInt(inputStream);
        final int rowId = readInt(inputStream);
        final int operation = readInt(inputStream);
        final int bookSide = readInt(inputStream);
        final double price = readDouble(inputStream);
        final int size = readInt(inputStream);

        ibSession.onMarketDepthUpdate(requestId, rowId, operation, bookSide, price, size);
    }
}