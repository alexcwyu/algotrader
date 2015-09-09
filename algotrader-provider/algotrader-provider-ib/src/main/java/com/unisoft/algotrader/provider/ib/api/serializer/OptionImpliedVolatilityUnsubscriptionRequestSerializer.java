package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.exception.RequestException;
import com.unisoft.algotrader.provider.ib.api.model.system.ClientMessageCode;
import com.unisoft.algotrader.provider.ib.api.model.system.Feature;
import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class OptionImpliedVolatilityUnsubscriptionRequestSerializer extends Serializer{

    private static final int VERSION = 1;
    public OptionImpliedVolatilityUnsubscriptionRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(long requestId){
        checkCalculateImpliedVolatility();

        ByteArrayBuilder builder = getByteArrayBuilder();

        builder.append(OutgoingMessageId.OPTION_IMPLIED_VOLATILITY_UNSUBSCRIPTION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(requestId);

        return builder.toBytes();
    }

    private void checkCalculateImpliedVolatility() {
        if (!Feature.CALCULATE_IMPLIED_VOLATILITY.isSupportedByVersion(getServerCurrentVersion())) {
            throw new RequestException(ClientMessageCode.UPDATE_TWS,
                    "It does not support calculate implied volatility requests.");
        }
    }
}
