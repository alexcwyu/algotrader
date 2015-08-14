package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class AccountUpdateTimeEventDeserializer extends Deserializer {


    public AccountUpdateTimeEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.ACCOUNT_UPDATE_TIME, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final String time = readString(inputStream);
        ibSession.onUpdateAccountTime(time);

    }
}