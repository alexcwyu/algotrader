package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.EventBusManager;

import java.io.InputStream;

/**
 * Created by alex on 8/11/15.
 */
public abstract class Deserializer<M extends Event> {

    public abstract M deserialize(final InputStream inputStream,
                                  final int serverCurrentVersion);

    public abstract  void publishEvent(final InputStream inputStream,
            final int serverCurrentVersion,
            final EventBusManager eventBusManager);
}
