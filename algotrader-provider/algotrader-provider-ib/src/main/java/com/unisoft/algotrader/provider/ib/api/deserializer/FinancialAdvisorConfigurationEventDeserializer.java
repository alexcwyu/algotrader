package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class FinancialAdvisorConfigurationEventDeserializer extends Deserializer {


    public FinancialAdvisorConfigurationEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.FINANCIAL_ADVISOR_CONFIGURATION, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int dataType = readInt(inputStream);
        final String xml = readString(inputStream);
        ibSession.onFinancialAdvisorConfiguration(dataType, xml);
    }
}