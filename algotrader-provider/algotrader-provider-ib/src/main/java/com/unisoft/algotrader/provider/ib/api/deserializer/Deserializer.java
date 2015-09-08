package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;

/**
 * Created by alex on 8/11/15.
 */
public abstract class Deserializer<M extends Event> {

    protected static final int VERSION_2 = 2;
    protected static final int VERSION_3 = 3;

    protected static final Logger LOG = LogManager.getLogger(Deserializer.class);

    private final IncomingMessageId messageId;

    public Deserializer(IncomingMessageId messageId){
        this.messageId = messageId;
    }

    public void consume(final InputStream inputStream,
                     final IBProvider ibProvider) {
        Validate.notNull(inputStream);
        int version = readInt(inputStream);
        consumeMessageContent(version, inputStream, ibProvider);
    }

    public IncomingMessageId messageId(){
        return messageId;
    }

    public abstract void consumeMessageContent(final int version, final InputStream inputStream,
                                               final IBProvider ibProvider);



}
