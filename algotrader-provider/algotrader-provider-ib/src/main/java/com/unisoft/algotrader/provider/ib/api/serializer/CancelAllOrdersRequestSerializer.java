package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.exception.RequestException;
import com.unisoft.algotrader.provider.ib.api.model.system.ClientMessageCode;
import com.unisoft.algotrader.provider.ib.api.model.system.Feature;
import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class CancelAllOrdersRequestSerializer extends Serializer{

    private static final int VERSION = 1;
    public CancelAllOrdersRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(){
        checkGlobalCancelOrderRequest();

        ByteArrayBuilder builder = getByteArrayBuilder();
        builder.append(OutgoingMessageId.CANCEL_ALL_ORDERS_REQUEST.getId());
        builder.append(VERSION);
        return builder.toBytes();
    }

    private void checkGlobalCancelOrderRequest() {
        if (!Feature.GLOBAL_CANCEL_ORDER_REQUEST.isSupportedByVersion(getServerCurrentVersion())) {
            throw new RequestException(ClientMessageCode.UPDATE_TWS, "It does not support globalCancel requests.");
        }
    }

}
