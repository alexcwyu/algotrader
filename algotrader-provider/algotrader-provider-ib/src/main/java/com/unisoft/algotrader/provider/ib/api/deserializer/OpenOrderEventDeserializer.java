package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class OpenOrderEventDeserializer extends Deserializer {

    private final RefDataStore refDataStore;

    public OpenOrderEventDeserializer(RefDataStore refDataStore, int serverCurrentVersion){
        super(IncomingMessageId.RETRIEVE_OPEN_ORDER, serverCurrentVersion);
        this.refDataStore = refDataStore;
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int orderId = readInt(inputStream);
        final Instrument instrument = parseInstrument(inputStream, refDataStore);
        Order order = parseOrder(inputStream);
        order = parseOrderExecution(inputStream, order);
        ibSession.onOpenOrder(order);
    }
}