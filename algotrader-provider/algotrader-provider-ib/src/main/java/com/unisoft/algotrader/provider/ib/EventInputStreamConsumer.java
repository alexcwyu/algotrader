package com.unisoft.algotrader.provider.ib;

import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.api.deserializer.Deserializer;
import com.unisoft.algotrader.provider.ib.api.deserializer.DeserializerFactory;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;
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
    private final IBEventHandler eventHandler;
    private final IBSocket ibSocket;
    private final InputStream inputStream;
    private boolean running = true;

    public EventInputStreamConsumer(
            IBEventHandler eventHandler,
            IBSocket ibSocket,
            int currentServerVersion,
            RefDataStore refDataStore){
        this.eventHandler = eventHandler;
        this.ibSocket = ibSocket;
        this.deserializerFactory = new DeserializerFactory(currentServerVersion, refDataStore);
        this.inputStream = ibSocket.getInputStream();
    }

    public void run(){
        try {
            while (running) {
                consumeMessage();
            }
        }
        catch(Exception e){
            LOG.error("fail to consume message", e);
            throw new RuntimeException(e);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private void consumeMessage() {
        final int messageId = readInt(inputStream);
        final IncomingMessageId incomingMessageId = IncomingMessageId.fromId(messageId);
        final Deserializer serializer =
                deserializerFactory.getDeserializer(incomingMessageId);
        LOG.info("consumeMessage, incomingMessageId {}", incomingMessageId);
        serializer.consume(inputStream, eventHandler);
    }

    public void stop(){
        running = false;
    }

}
