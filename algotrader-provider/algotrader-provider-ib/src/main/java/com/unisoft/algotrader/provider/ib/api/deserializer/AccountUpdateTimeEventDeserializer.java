package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class AccountUpdateTimeEventDeserializer extends Deserializer {


    public AccountUpdateTimeEventDeserializer(){
        super(IncomingMessageId.ACCOUNT_UPDATE_TIME);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBSession ibSession) {
        final String time = readString(inputStream);
        ibSession.onUpdateAccountTime(time);

    }
}