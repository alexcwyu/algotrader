package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.constants.FinancialAdvisorDataType;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class FinancialAdvisorConfigurationEventDeserializer extends Deserializer {


    public FinancialAdvisorConfigurationEventDeserializer(){
        super(IncomingMessageId.FINANCIAL_ADVISOR_CONFIGURATION);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final int dataType = readInt(inputStream);
        final String xml = readString(inputStream);
        ibProvider.onFinancialAdvisorConfigurationEvent(FinancialAdvisorDataType.fromValue(dataType), xml);
    }
}