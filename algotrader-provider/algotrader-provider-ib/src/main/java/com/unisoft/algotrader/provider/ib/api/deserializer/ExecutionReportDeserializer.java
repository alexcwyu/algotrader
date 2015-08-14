package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;


/**
 * Created by alex on 8/13/15.
 */
public class ExecutionReportDeserializer extends Deserializer {


    public ExecutionReportDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.EXECUTION_REPORT, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final String key = readString(inputStream);
        final String value = readString(inputStream);
        final String currency = readString(inputStream);
        final String accountName = (getVersion() >= 2) ? readString(inputStream) : null;

        ibSession.onUpdateAccountValue(key, value, currency, accountName);

    }
}