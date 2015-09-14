package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.IBEvent;
import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;

/**
 * Created by alex on 8/11/15.
 */
public abstract class Deserializer<M extends IBEvent>{

    protected static final int VERSION_2 = 2;
    protected static final int VERSION_3 = 3;

    protected static final Logger LOG = LogManager.getLogger(Deserializer.class);

    private final IncomingMessageId messageId;

    protected final int serverCurrentVersion;

    public Deserializer(IncomingMessageId messageId, int serverCurrentVersion){
        this.messageId = messageId;
        this.serverCurrentVersion = serverCurrentVersion;
    }

    public void consume(final InputStream inputStream,
                     final IBEventHandler eventHandler) {
        try {
            Validate.notNull(inputStream);
            int version = readInt(inputStream);
            consumeMessageContent(version, inputStream, eventHandler);
        }
        catch(Exception e){
            LOG.error("fail to consume message", e);
            throw new RuntimeException(e);
        }
    }

    public IncomingMessageId messageId(){
        return messageId;
    }

    public abstract void consumeMessageContent(final int version, final InputStream inputStream,
                                               final IBEventHandler eventHandler);


}
