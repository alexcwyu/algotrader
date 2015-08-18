package com.unisoft.algotrader.provider.ib.api.deserializer;
import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;

import java.io.InputStream;
import java.util.List;

import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class HistoricalDataEventDeserializer extends Deserializer {


    //private static final String FINISHED = "finished";
    public HistoricalDataEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.HISTORICAL_DATA, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final List<Bar> historicalDataEvents = Lists.newArrayList();
        final int requestId = readInt(inputStream);
        String startDate = null;
        String endDate = null;
        //String finishedRetrievingHistoricalData = FINISHED;
        if (getVersion() >= 2) {
            startDate = readString(inputStream);
            endDate = readString(inputStream);
            //finishedRetrievingHistoricalData = finishedRetrievingHistoricalData + "-" + startDate + "-" + endDate;
        }
        final int historicalDatas = readInt(inputStream);
        for (int i = 0; i < historicalDatas; i++) {
            historicalDataEvents.add(consumeHistoricalData(requestId, inputStream));
        }
        ibSession.onhistoricalDataEvents(requestId, historicalDataEvents);
    }

    private Bar consumeHistoricalData(final int requestId, final InputStream inputStream) {
        final String dateTime = readString(inputStream);
        final double open = readDouble(inputStream);
        final double high = readDouble(inputStream);
        final double low = readDouble(inputStream);
        final double close = readDouble(inputStream);
        final int volume = readInt(inputStream);
        final double weightedAveragePrice = readDouble(inputStream);
        final String hasGap = readString(inputStream);
        int tradeNumber = -1;
        if (getVersion() >= 3) {
            tradeNumber = readInt(inputStream);
        }

        //TODO instID lookup from requestId
        long instId = 0;
        //TODO size lookup from rquestId
        int size = 0;

        //TODO datetime to time
        long time = 0;

        return new Bar(instId, size, time, open, high, low, close, volume);
    }
}