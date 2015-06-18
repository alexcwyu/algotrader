package com.unisoft.algotrader.provider.csv;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.event.EventBusManager;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.MarketDataContainer;
import com.unisoft.algotrader.provider.SubscriptionKey;
import com.unisoft.algotrader.provider.csv.historical.HistoricalDataProvider;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.FileReader;
import java.io.Reader;
import java.util.Date;
import java.util.List;

import static com.unisoft.algotrader.provider.csv.CSVConfig.FORMAT;
/**
 * Created by alex on 5/19/15.
 */
public class CSVHistoricalDataProvider implements HistoricalDataProvider {


    private final String path;
    private final RingBuffer<MarketDataContainer> marketDataRB;

    public CSVHistoricalDataProvider(String path){
        this(path, EventBusManager.INSTANCE.rawMarketDataRB);
    }
    public CSVHistoricalDataProvider(String path, RingBuffer<MarketDataContainer> marketDataRB){
        this.path = path;
        this.marketDataRB = marketDataRB;
    }

    @Override
    public void subscribe(SubscriptionKey subscriptionKey, Date fromDate, Date toDate) {
        CsvParserSettings settings = new CsvParserSettings();

        settings.getFormat().setLineSeparator("\n");
        settings.getFormat().setDelimiter(',');
        settings.setHeaderExtractionEnabled(true);

        try {

            long fromDateTime = fromDate.getTime();
            long toDateTime = toDate.getTime();
            String[] row;
            CsvParser parser = new CsvParser(settings);
            Reader reader = new FileReader(path+CSVConfig.getFileName(subscriptionKey));
            //parser.beginParsing(reader);

            List<String[]> allRows = parser.parseAll(reader);
            for (int i = allRows.size() -1; i >=0; i--) {
                row = allRows.get(i);
                long time = FORMAT.parse(row[0]).getTime();
                if (time <fromDateTime)
                    continue;
                if (time >toDateTime)
                    break;

                long sequence = marketDataRB.next();

                MarketDataContainer event = marketDataRB.get(sequence);
                event.reset();
                event.dateTime = time;
                event.instId = subscriptionKey.instId;
                event.bitset.set(MarketDataContainer.BAR_BIT);

                Bar bar = event.bar;
                bar.instId = subscriptionKey.instId;
                bar.dateTime = time;
                bar.size = SubscriptionKey.DAILY_SIZE;
                bar.high = Double.parseDouble(row[2]);
                bar.low = Double.parseDouble(row[3]);
                bar.open = Double.parseDouble(row[1]);
                bar.close = Double.parseDouble(row[4]);
                bar.volume = Long.parseLong(row[5]);
                bar.openInt = 0;

                marketDataRB.publish(sequence);

            }
            //parser.stopParsing();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String providerId() {
        return "CSV";
    }

    @Override
    public boolean connected(){
        return true;
    }

}
