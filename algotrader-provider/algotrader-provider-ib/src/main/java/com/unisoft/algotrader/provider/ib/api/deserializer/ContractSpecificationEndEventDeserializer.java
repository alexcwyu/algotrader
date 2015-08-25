package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;


/**
 * Created by alex on 8/13/15.
 */
public class ContractSpecificationEndEventDeserializer extends Deserializer {


    public ContractSpecificationEndEventDeserializer(){
        super(IncomingMessageId.CONTRACT_SPECIFICATION_END);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBSession ibSession) {
        final int requestId = readInt(inputStream);
        ibSession.onContractSpecificationEndEvent(requestId);
    }
}