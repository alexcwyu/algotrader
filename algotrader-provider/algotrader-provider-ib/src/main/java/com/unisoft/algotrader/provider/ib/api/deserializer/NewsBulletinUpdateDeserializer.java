package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSocket;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class NewsBulletinUpdateDeserializer extends Deserializer {


    public NewsBulletinUpdateDeserializer(){
        super(IncomingMessageId.NEWS_BULLETIN_UPDATE);
    }

    @Override
    public void consumeVersionLess(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final int newsBulletinId = readInt(inputStream);
        final int newsBulletinTypeValue = readInt(inputStream);
        final String message = readString(inputStream);
        final String exchange = readString(inputStream);

        ibProvider.onNewsBulletinUpdateEvent(newsBulletinId, IBConstants.NewsBulletinType.fromValue(newsBulletinTypeValue),
                message, exchange);
    }
}