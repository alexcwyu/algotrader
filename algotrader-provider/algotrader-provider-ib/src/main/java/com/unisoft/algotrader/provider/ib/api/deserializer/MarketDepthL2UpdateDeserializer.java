package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSocket;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class MarketDepthL2UpdateDeserializer extends Deserializer {


    public MarketDepthL2UpdateDeserializer(){
        super(IncomingMessageId.MARKET_DEPTH_LEVEL_TWO_UPDATE);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final int requestId = readInt(inputStream);
        final int rowId = readInt(inputStream);
        final String marketMakerName = readString(inputStream);
        final int operation = readInt(inputStream);
        final int bookSide = readInt(inputStream);
        final double price = readDouble(inputStream);
        final int size = readInt(inputStream);

        ibProvider.onMarketDepthLevelTwoUpdateEvent(requestId, rowId, marketMakerName, IBConstants.Operation.fromValue(operation),
                IBConstants.BookSide.fromValue(bookSide), price, size);
    }
}