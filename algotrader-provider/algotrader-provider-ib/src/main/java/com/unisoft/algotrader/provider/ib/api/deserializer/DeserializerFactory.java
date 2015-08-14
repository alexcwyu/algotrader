package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.util.Map;

/**
 * Created by alex on 8/2/15.
 */
public class DeserializerFactory {

    private final Map<IncomingMessageId, Deserializer<? extends Event>> deserializerCache = Maps.newHashMap();

    private final RefDataStore refDataStore;
    private final int serverCurrentVersion;

    public DeserializerFactory(RefDataStore refDataStore, int serverCurrentVersion){
        this.refDataStore = refDataStore;
        this.serverCurrentVersion = serverCurrentVersion;
    }

    public <M extends Event> Deserializer<M> getDeserializer(final IncomingMessageId messageId) {
        if (deserializerCache.containsKey(messageId)) {
            return (Deserializer<M>) deserializerCache.get(messageId);
        }
        throw new IllegalArgumentException("unsupported messageID: "+messageId);
    }

}
