package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readInt;
import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class ContractSpecificationEndEventDeserializer extends Deserializer {


    public ContractSpecificationEndEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.CONTRACT_SPECIFICATION_END, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int requestId = readInt(inputStream);
        ibSession.onContractSpecificationEndEvent(requestId);
    }
}