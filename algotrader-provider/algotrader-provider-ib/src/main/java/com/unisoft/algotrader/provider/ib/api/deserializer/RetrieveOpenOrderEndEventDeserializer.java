package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readInt;

/**
 * Created by alex on 8/13/15.
 */
public class RetrieveOpenOrderEndEventDeserializer extends Deserializer {


    public RetrieveOpenOrderEndEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.RETRIEVE_OPEN_ORDER_END, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        ibSession.onRetrieveOpenOrderEndEvent();
    }
}