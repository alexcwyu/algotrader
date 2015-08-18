package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class NewsBulletinUpdateDeserializer extends Deserializer {


    public NewsBulletinUpdateDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.NEWS_BULLETIN_UPDATE, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int newsBulletinId = readInt(inputStream);
        final int newsBulletinTypeValue = readInt(inputStream);
        final String message = readString(inputStream);
        final String exchange = readString(inputStream);

        ibSession.onNewsBulletinUpdate(newsBulletinId, newsBulletinTypeValue,
        message, exchange);
    }
}