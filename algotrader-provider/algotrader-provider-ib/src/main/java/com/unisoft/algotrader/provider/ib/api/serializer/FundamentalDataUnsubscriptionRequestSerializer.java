package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.exception.RequestException;
import com.unisoft.algotrader.provider.ib.api.model.system.ClientMessageCode;
import com.unisoft.algotrader.provider.ib.api.model.system.Feature;
import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class FundamentalDataUnsubscriptionRequestSerializer extends Serializer{

    private static final int VERSION = 1;
    public FundamentalDataUnsubscriptionRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion, OutgoingMessageId.FUNDAMENTAL_DATA_UNSUBSCRIPTION_REQUEST);
    }

    public byte [] serialize(long id){
        checkReutersFundamentalDataSupport();

        ByteArrayBuilder builder = getByteArrayBuilder();
        builder.append(messageId.getId());
        builder.append(VERSION);
        builder.append(id);

        return builder.toBytes();
    }


    private void checkReutersFundamentalDataSupport() {
        if (!Feature.REUTERS_FUNDAMENTAL_DATA.isSupportedByVersion(getServerCurrentVersion())) {
            throw new RequestException(ClientMessageCode.UPDATE_TWS, "It does not support fundamental data requests.");
        }
    }
}
