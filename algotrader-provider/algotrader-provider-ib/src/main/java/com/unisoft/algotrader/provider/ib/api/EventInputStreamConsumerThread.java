package com.unisoft.algotrader.provider.ib.api;

import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.EventBus;
import com.unisoft.algotrader.provider.ib.api.message.EventInputStreamConsumer;
import com.unisoft.algotrader.provider.ib.api.message.EventInputStreamConsumerService;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;

/**
 * Created by alex on 8/1/15.
 */
public class EventInputStreamConsumerThread implements Runnable {

    private final EventInputStreamConsumerService eventInputStreamConsumerService;
    private final InputStream inputStream;
    private boolean running = true;

    public EventInputStreamConsumerThread(
            EventInputStreamConsumerService eventInputStreamConsumerService,
            InputStream inputStream){
        this.eventInputStreamConsumerService = eventInputStreamConsumerService;
        this.inputStream = inputStream;
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
        final EventInputStreamConsumer consumer =
                eventInputStreamConsumerService.getEventCreatingConsumer(IncomingMessageId.fromId(messageId));
        consumer.consume(inputStream);
    }

}
