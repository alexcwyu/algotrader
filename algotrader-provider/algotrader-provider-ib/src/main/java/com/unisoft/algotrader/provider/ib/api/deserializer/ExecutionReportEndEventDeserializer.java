package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.event.IBEventHandler;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.readInt;


/**
 * Created by alex on 8/13/15.
 */
public class ExecutionReportEndEventDeserializer extends Deserializer {


    public ExecutionReportEndEventDeserializer(){
        super(IncomingMessageId.EXECUTION_REPORT_END);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBEventHandler eventHandler) {
        final int requestId = readInt(inputStream);
        eventHandler.onExecutionReportEndEvent(requestId);
    }
}