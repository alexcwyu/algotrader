package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.bulletin.NewsBulletinType;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;
import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readString;

/**
 * Created by alex on 8/13/15.
 */
public class NewsBulletinUpdateDeserializer extends Deserializer {


    public NewsBulletinUpdateDeserializer(){
        super(IncomingMessageId.NEWS_BULLETIN_UPDATE);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final int newsBulletinId = readInt(inputStream);
        final int newsBulletinTypeValue = readInt(inputStream);
        final String message = readString(inputStream);
        final String exchange = readString(inputStream);

        eventHandler.onNewsBulletinUpdateEvent(newsBulletinId, NewsBulletinType.fromValue(newsBulletinTypeValue),
                message, exchange);
    }
}