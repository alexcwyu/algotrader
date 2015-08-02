package com.unisoft.algotrader.provider.ib.api.message;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.util.Map;

/**
 * Created by alex on 8/2/15.
 */
public class EventInputStreamConsumerService {

    private final Map<IncomingMessageId, EventInputStreamConsumer<? extends Event>> consumerCache = Maps.newHashMap();

    private int serverCurrentVersion;

    public EventInputStreamConsumerService(int serverCurrentVersion){
        this.serverCurrentVersion = serverCurrentVersion;
    }

    public <M extends Event> EventInputStreamConsumer<M> getEventCreatingConsumer(final IncomingMessageId messageId) {
        if (consumerCache.containsKey(messageId)) {
            return (EventInputStreamConsumer<M>) consumerCache.get(messageId);
        }
        throw new IllegalArgumentException("unsupported messageID: "+messageId);
    }

}
