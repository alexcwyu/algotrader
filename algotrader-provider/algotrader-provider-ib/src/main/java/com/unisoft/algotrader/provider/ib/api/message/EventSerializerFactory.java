package com.unisoft.algotrader.provider.ib.api.message;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.util.Map;

/**
 * Created by alex on 8/2/15.
 */
public class EventSerializerFactory {

    private final Map<IncomingMessageId, EventSerializer<? extends Event>> eventSerializerCache = Maps.newHashMap();

    private int serverCurrentVersion;

    public EventSerializerFactory(int serverCurrentVersion){
        this.serverCurrentVersion = serverCurrentVersion;
    }

    public <M extends Event> EventSerializer<M> getEventSerializer(final IncomingMessageId messageId) {
        if (eventSerializerCache.containsKey(messageId)) {
            return (EventSerializer<M>) eventSerializerCache.get(messageId);
        }
        throw new IllegalArgumentException("unsupported messageID: "+messageId);
    }

}
