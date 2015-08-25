package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.util.Map;

/**
 * Created by alex on 8/2/15.
 */
public class DeserializerFactory {

    private final Map<IncomingMessageId, Deserializer<? extends Event>> deserializerCache = Maps.newHashMap();

    public <M extends Event> Deserializer<M> getDeserializer(final IncomingMessageId messageId) {
        if (deserializerCache.containsKey(messageId)) {
            return (Deserializer<M>) deserializerCache.get(messageId);
        }
        throw new IllegalArgumentException("unsupported messageID: "+messageId);
    }

}
