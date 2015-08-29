package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBSocket;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;
import org.apache.commons.lang3.Validate;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.readInt;

/**
 * Created by alex on 8/11/15.
 */
public abstract class Deserializer<M extends Event> {

    protected static final int VERSION_2 = 2;
    protected static final int VERSION_3 = 3;

    private final IncomingMessageId messageId;

    public Deserializer(IncomingMessageId messageId){
        this.messageId = messageId;
    }

    public void consume(final InputStream inputStream,
                     final IBProvider ibProvider) {
        Validate.notNull(inputStream);
        int version = readInt(inputStream);
        consumeVersionLess(version, inputStream, ibProvider);
    }

    public IncomingMessageId messageId(){
        return messageId;
    }

    public abstract void consumeVersionLess(final int version, final InputStream inputStream,
            final IBProvider ibProvider);



}
