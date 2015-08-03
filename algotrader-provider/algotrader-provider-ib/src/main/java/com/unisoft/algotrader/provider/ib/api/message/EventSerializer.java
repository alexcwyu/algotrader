package com.unisoft.algotrader.provider.ib.api.message;

import ch.aonyx.broker.ib.api.Id;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.EventBusManager;

import java.io.InputStream;

import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readInt;

/**
 * Created by alex on 8/2/15.
 */
public abstract class EventSerializer<M extends Event> {

    protected int serverCurrentVersion;
    protected EventBusManager eventBusManager;

    protected EventSerializer(int serverCurrentVersion, EventBusManager eventBusManager) {
        this.serverCurrentVersion = serverCurrentVersion;
        this.eventBusManager = eventBusManager;
    }

    public abstract void publishEvent(InputStream inputStream, int serverCurrentVersion, EventBusManager eventBusManager);

    protected int parseVersion(InputStream inputStream){
        return readInt(inputStream);
    }

    public abstract byte[] serialize(M event);

    public abstract M deserialize(InputStream inputStream);

    public abstract M deserialize(byte[] bytes);

}
