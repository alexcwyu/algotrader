package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;
import com.unisoft.algotrader.provider.ib.api.model.constants.TickType;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readDouble;
import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;

/**
 * Created by alex on 8/13/15.
 */
public class TickPriceDeserializer extends Deserializer {


    public TickPriceDeserializer(){
        super(IncomingMessageId.TICK_PRICE);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {

        final int requestId = readInt(inputStream);
        final int tickType = readInt(inputStream);
        final double price = readDouble(inputStream);

        final int size = (version >= VERSION_2) ? readInt(inputStream): 0;
        final int autoExecute = (version >= VERSION_3) ? readInt(inputStream) : 0;

        TickType tickPriceType = TickType.fromValue(tickType);
        ibProvider.onTickPriceEvent(requestId, tickPriceType, price, autoExecute);

        if (version >= VERSION_2) {
            int sizeTickType = TickType.UNKNOWN.getValue();
            switch (tickPriceType) {
                case BID_PRICE:
                    sizeTickType = TickType.BID_SIZE.getValue();
                    break;
                case ASK_PRICE:
                    sizeTickType = TickType.ASK_SIZE.getValue();
                    break;
                case LAST_PRICE:
                    sizeTickType = TickType.LAST_SIZE.getValue();
                    break;
                default:
                    sizeTickType = TickType.UNKNOWN.getValue();
                    break;
            }
            if (sizeTickType != TickType.UNKNOWN.getValue()) {
                ibProvider.onTickSizeEvent(requestId, TickType.fromValue(sizeTickType), size);
            }
        }
    }
}