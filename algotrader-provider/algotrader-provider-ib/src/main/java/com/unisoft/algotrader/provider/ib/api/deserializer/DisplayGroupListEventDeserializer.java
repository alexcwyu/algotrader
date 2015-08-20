package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class DisplayGroupListEventDeserializer extends Deserializer {


    public DisplayGroupListEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.DISPLAY_GROUP_LIST, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int requestId = readInt(inputStream);
        final String contractInfo = readString(inputStream);;
        
        ibSession.onDisplayGroupList(requestId, contractInfo);
    }
}