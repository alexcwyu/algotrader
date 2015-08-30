package com.unisoft.algotrader.provider.ib;

import com.unisoft.algotrader.provider.ib.api.deserializer.Deserializer;
import com.unisoft.algotrader.provider.ib.api.deserializer.DeserializerFactory;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;

/**
 * Created by alex on 8/1/15.
 */
public class EventInputStreamConsumer implements Runnable {

    private static final Logger LOG = LogManager.getLogger(EventInputStreamConsumer.class);
    
    private final DeserializerFactory deserializerFactory;
    private final IBProvider ibProvider;
    private final IBSocket ibSocket;
    private final InputStream inputStream;
    private boolean running = true;

    public EventInputStreamConsumer(
            IBProvider ibProvider,
            IBSocket ibSocket){
        this.ibProvider = ibProvider;
        this.ibSocket = ibSocket;
        this.deserializerFactory = new DeserializerFactory();
        this.inputStream = ibSocket.getInputStream();
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
        serializer.consume(inputStream, ibProvider);
    }

}
