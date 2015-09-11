package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.FinancialAdvisorConfigurationEvent;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.fa.FinancialAdvisorDataType;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class FinancialAdvisorConfigurationEventDeserializer extends Deserializer<FinancialAdvisorConfigurationEvent> {


    public FinancialAdvisorConfigurationEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.FINANCIAL_ADVISOR_CONFIGURATION, serverCurrentVersion);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final int dataType = readInt(inputStream);
        final String xml = readString(inputStream);
        eventHandler.onFinancialAdvisorConfigurationEvent(FinancialAdvisorDataType.fromValue(dataType), xml);
    }
}