package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static ch.aonyx.broker.ib.api.util.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class ExecutionReportEndEventDeserializer extends Deserializer {


    public ExecutionReportEndEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.EXECUTION_REPORT_END, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final int requestId = readInt(inputStream);
        ibSession.onExecutionReportEndEvent(requestId);
    }
}