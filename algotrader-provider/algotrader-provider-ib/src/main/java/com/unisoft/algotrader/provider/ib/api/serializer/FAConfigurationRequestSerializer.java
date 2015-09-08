package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.fa.FinancialAdvisorDataType;
import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class FAConfigurationRequestSerializer extends Serializer{

    private static final int VERSION = 1;

    public FAConfigurationRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(FinancialAdvisorDataType type){

        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.FINANCIAL_ADVISOR_CONFIGURATION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(type.getValue());

        return builder.toBytes();
    }

}
