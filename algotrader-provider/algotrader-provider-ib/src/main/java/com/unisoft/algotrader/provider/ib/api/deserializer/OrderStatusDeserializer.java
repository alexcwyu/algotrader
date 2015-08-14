package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class OrderStatusDeserializer extends Deserializer {


    public OrderStatusDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.ORDER_STATE_UPDATE, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int orderId = readInt(inputStream);
        final String orderStatus = readString(inputStream);
        final int filledQuantity = readInt(inputStream);
        final int remainingQuantity = readInt(inputStream);
        final double averageFilledPrice = readDouble(inputStream);
        final int permanentId = (getVersion() >= 2) ? readInt(inputStream) : 0;
        final int parentOrderId = (getVersion() >= 3) ? readInt(inputStream) : 0;
        final double lastFilledPrice = (getVersion() >= 4) ? readDouble(inputStream) : 0;
        final int clientId =  (getVersion() >= 5) ? readInt(inputStream) : 0;
        final String heldCause = (getVersion() >= 6) ? readString(inputStream) : null;

        ibSession.onOrderStatus(orderId, orderStatus, filledQuantity, remainingQuantity, averageFilledPrice, permanentId,
                parentOrderId, lastFilledPrice, clientId, heldCause);
    }
}