package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.fa.FinancialAdvisorDataType;
import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class FAReplaceConfigurationRequestSerializer extends Serializer{

    private static final int VERSION = 1;

    public FAReplaceConfigurationRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(FinancialAdvisorDataType type, String xml){

        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.FINANCIAL_ADVISOR_REPLACE_CONFIGURATION_REQUEST.getId());
        builder.append(VERSION);
        builder.append(type.getValue());
        builder.append(xml);
        return builder.toBytes();
    }

}
