package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.exception.RequestException;
import com.unisoft.algotrader.provider.ib.api.model.system.ClientMessageCode;
import com.unisoft.algotrader.provider.ib.api.model.system.Feature;
import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class OptionPriceUnsubscriptionRequestSerializer extends Serializer{

    private static final int VERSION = 1;
    public OptionPriceUnsubscriptionRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(long requestId){
        checkCancelCalculateOptionPrice();

        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.OPTION_PRICE_UNSUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(requestId);

        return builder.toBytes();
    }

    private void checkCancelCalculateOptionPrice() {
        if (!Feature.CALCULATE_OPTION_PRICE.isSupportedByVersion(getServerCurrentVersion())) {
            throw new RequestException(ClientMessageCode.UPDATE_TWS,
                    "It does not support calculate option price requests.");
        }
    }
}
