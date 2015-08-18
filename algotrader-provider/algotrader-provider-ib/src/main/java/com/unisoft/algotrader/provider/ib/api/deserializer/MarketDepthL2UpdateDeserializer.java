package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class MarketDepthL2UpdateDeserializer extends Deserializer {


    public MarketDepthL2UpdateDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.MARKET_DEPTH_LEVEL_TWO_UPDATE, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int requestId = readInt(inputStream);
        final int rowId = readInt(inputStream);
        final String marketMakerName = readString(inputStream);
        final int operation = readInt(inputStream);
        final int bookSide = readInt(inputStream);
        final double price = readDouble(inputStream);
        final int size = readInt(inputStream);

        ibSession.onMarketDepthL2Update(requestId, rowId, marketMakerName, operation, bookSide, price, size);
    }
}