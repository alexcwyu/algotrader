package com.unisoft.algotrader.provider.ib.api;

import com.unisoft.algotrader.model.event.EventBusManager;
import com.unisoft.algotrader.provider.ib.api.message.EventSerializer;
import com.unisoft.algotrader.provider.ib.api.message.EventSerializerFactory;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;

/**
 * Created by alex on 8/1/15.
 */
public class EventInputStreamConsumerThread implements Runnable {

    private final EventSerializerFactory eventSerializerFactory;
    private final InputStream inputStream;
    private final int serverCurrentVersion;
    private final EventBusManager eventBusManager;
    private boolean running = true;

    public EventInputStreamConsumerThread(
            EventSerializerFactory eventSerializerFactory,
            InputStream inputStream, int serverCurrentVersion,
            EventBusManager eventBusManager){
        this.eventSerializerFactory = eventSerializerFactory;
        this.inputStream = inputStream;
        this.serverCurrentVersion = serverCurrentVersion;
        this.eventBusManager = eventBusManager;
    }

    public void run(){
        try {
            while (running) {
                consumeMessage();
            }
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private void consumeMessage() {
        final int messageId = readInt(inputStream);
        final EventSerializer eventSerializer =
                eventSerializerFactory.getEventSerializer(IncomingMessageId.fromId(messageId));
        eventSerializer.publishEvent(inputStream, serverCurrentVersion, eventBusManager);
    }

}
