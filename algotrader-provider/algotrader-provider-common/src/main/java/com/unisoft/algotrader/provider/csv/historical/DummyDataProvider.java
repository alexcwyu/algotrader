package com.unisoft.algotrader.provider.csv.historical;

import com.unisoft.algotrader.event.EventBus;
import com.unisoft.algotrader.provider.SubscriptionKey;
import com.univocity.parsers.csv.CsvParserSettings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by alex on 5/19/15.
 */
public class DummyDataProvider implements HistoricalDataProvider {

    private final static long DAY_TO_MS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
    private final static int DAILY_SIZE = 60 * 60 * 24;

    public static SimpleDateFormat FORMAT2 = new SimpleDateFormat("yyyyMMdd");


    @Override
    public void subscribe(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, Date fromDate, Date toDate) {
        CsvParserSettings settings = new CsvParserSettings();

        settings.getFormat().setLineSeparator("\n");
        settings.getFormat().setDelimiter(',');
        settings.setHeaderExtractionEnabled(true);

        long dateTime = fromDate.getTime();
        long toDateTime = toDate.getTime();

        int count = 0;
        while (dateTime < toDateTime) {

            eventBus.publishBar(subscriptionKey.instId, subscriptionKey.barSize,dateTime,
                    900 + count,
                    1000 + count,
                    800 + count,
                    950 + count,
                    0,0);

            dateTime += DAY_TO_MS;
            count++;
        }
    }

    @Override
    public String providerId() {
        return "Dummy";
    }

    @Override
    public boolean connected(){
        return true;
    }

}
