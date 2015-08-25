package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class OrderStatusDeserializer extends Deserializer {


    public OrderStatusDeserializer(){
        super(IncomingMessageId.ORDER_STATE_UPDATE);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBSession ibSession) {
        final int orderId = readInt(inputStream);
        final String orderStatus = readString(inputStream);
        final int filledQuantity = readInt(inputStream);
        final int remainingQuantity = readInt(inputStream);
        final double averageFilledPrice = readDouble(inputStream);
        final int permanentId = (version >= 2) ? readInt(inputStream) : 0;
        final int parentOrderId = (version >= 3) ? readInt(inputStream) : 0;
        final double lastFilledPrice = (version >= 4) ? readDouble(inputStream) : 0;
        final int clientId =  (version >= 5) ? readInt(inputStream) : 0;
        final String heldCause = (version >= 6) ? readString(inputStream) : null;

        ibSession.onOrderStatus(orderId, orderStatus, filledQuantity, remainingQuantity, averageFilledPrice, permanentId,
                parentOrderId, lastFilledPrice, clientId, heldCause);
    }
}