package com.unisoft.algotrader.provider.ib.api;

import com.unisoft.algotrader.provider.ib.api.deserializer.Deserializer;
import com.unisoft.algotrader.provider.ib.api.deserializer.DeserializerFactory;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;

/**
 * Created by alex on 8/1/15.
 */
public class EventInputStreamConsumer implements Runnable {

    private final DeserializerFactory deserializerFactory;
    private final IBSession ibSession;
    private final InputStream inputStream;
    private boolean running = true;

    public EventInputStreamConsumer(
            IBSession ibSession){
        this.ibSession = ibSession;
        this.deserializerFactory = new DeserializerFactory();
        this.inputStream = ibSession.getInputStream();
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
        final Deserializer serializer =
                deserializerFactory.getDeserializer(IncomingMessageId.fromId(messageId));
        serializer.consume(inputStream, ibSession);
    }

}
