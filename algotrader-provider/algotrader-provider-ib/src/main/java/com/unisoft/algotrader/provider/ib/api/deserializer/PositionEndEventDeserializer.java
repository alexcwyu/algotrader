package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

/**
 * Created by alex on 8/13/15.
 */
public class PositionEndEventDeserializer extends Deserializer {


    public PositionEndEventDeserializer(){
        super(IncomingMessageId.POSITION_END);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBSession ibSession) {
        ibSession.onPositionEnd();
    }
}