package com.unisoft.algotrader.provider.ib.api.deserializer;

import ch.aonyx.broker.ib.api.contract.UnderlyingCombo;
import ch.aonyx.broker.ib.api.order.*;
import com.unisoft.algotrader.model.event.Event;
import com.unisoft.algotrader.model.event.execution.Order;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.model.trading.*;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.InputStream;

import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readDoubleMax;
import static ch.aonyx.broker.ib.api.util.InputStreamUtils.readString;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/11/15.
 */
public abstract class Deserializer<M extends Event> {

    protected static final int VERSION_2 = 2;
    protected static final int VERSION_3 = 3;

    private final IncomingMessageId messageId;
    private int version;
    private final int serverCurrentVersion;

    public Deserializer(IncomingMessageId messageId, int serverCurrentVersion){
        this.messageId = messageId;
        this.serverCurrentVersion = serverCurrentVersion;
    }

    public void consume(final InputStream inputStream,
                     final IBSession ibSession) {
        Validate.notNull(inputStream);
        version = readInt(inputStream);
        consumeVersionLess(inputStream, ibSession);
    }

    protected final int getVersion() {
        return version;
    }

    protected final int getServerCurrentVersion() {
        return serverCurrentVersion;
    }

    public IncomingMessageId messageId(){
        return messageId;
    }

    public abstract void consumeVersionLess(final InputStream inputStream,
            final IBSession ibSession);



}
