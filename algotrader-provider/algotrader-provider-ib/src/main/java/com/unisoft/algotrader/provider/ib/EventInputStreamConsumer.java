package com.unisoft.algotrader.provider.ib;

import com.unisoft.algotrader.provider.ib.api.deserializer.Deserializer;
import com.unisoft.algotrader.provider.ib.api.deserializer.DeserializerFactory;
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
        serializer.consume(inputStream, ibProvider);
    }

    public void stop(){
        running = false;
    }

}
