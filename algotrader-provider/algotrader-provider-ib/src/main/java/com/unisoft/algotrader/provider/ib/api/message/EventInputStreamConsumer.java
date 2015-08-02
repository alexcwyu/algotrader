package com.unisoft.algotrader.provider.ib.api.message;

import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.EventBusManager;
import org.apache.commons.lang3.Validate;

import java.io.InputStream;

import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readInt;

/**
 * Created by alex on 8/2/15.
 */
public abstract class EventInputStreamConsumer<M extends Event> {

    private int version;

    private final int serverCurrentVersion;
    private final EventBusManager eventBusManager;

    public EventInputStreamConsumer(int serverCurrentVersion, EventBusManager eventBusManager){
        this.serverCurrentVersion = serverCurrentVersion;
        this.eventBusManager = eventBusManager;
    }

    public void consume(InputStream inputStream){
        Validate.notNull(inputStream);
        version = readInt(inputStream);
        process(inputStream);
    }


    protected abstract void process(InputStream inputStream);

}
