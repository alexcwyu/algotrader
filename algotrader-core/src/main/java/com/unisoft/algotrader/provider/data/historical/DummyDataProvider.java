package com.unisoft.algotrader.provider.data.historical;

import com.lmax.disruptor.RingBuffer;
import com.unisoft.algotrader.event.EventBusManager;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.MarketDataContainer;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.FileReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by alex on 5/19/15.
 */
public class DummyDataProvider implements HistoricalDataProvider{

    private final int count;
    private final RingBuffer<MarketDataContainer> marketDataRB;

    private final static  int DAILY_SIZE = 60*60*24;
    public DummyDataProvider(int count){
        this(count, EventBusManager.INSTANCE.rawMarketDataRB);
    }
    public DummyDataProvider(int count, RingBuffer<MarketDataContainer> marketDataRB){
        this.count = count;
        this.marketDataRB = marketDataRB;
    }

    public static SimpleDateFormat FORMAT2= new SimpleDateFormat("yyyyMMdd");
    @Override
    public void subscribe(String instId, int fromDate, int toDate) {
        CsvParserSettings settings = new CsvParserSettings();

        settings.getFormat().setLineSeparator("\n");
        settings.getFormat().setDelimiter(',');
        settings.setHeaderExtractionEnabled(true);

        try {

            long fromDateTime = FORMAT2.parse(Integer.toString(fromDate)).getTime();

            for (int i = 0 ; i <count; i++) {

                long time = fromDateTime + i;
                long sequence = marketDataRB.next();

                MarketDataContainer event = marketDataRB.get(sequence);
                event.reset();
                event.dateTime = fromDateTime;
                event.instId = instId;
                event.bitset.set(MarketDataContainer.BAR_BIT);

                Bar bar = event.bar;
                bar.instId = instId;
                bar.dateTime = time;
                bar.size = DAILY_SIZE;
                bar.high = 1000 + i;
                bar.low = 800 +i;
                bar.open = 900 +i;
                bar.close = 950 + i;
                bar.volume = 0;
                bar.openInt = 0;

                marketDataRB.publish(sequence);

            }
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

    public static void main(String [] args){
        DummyDataProvider provider = new DummyDataProvider(1000);
        provider.subscribe("HSI", 20110101, 20141231);
    }
}
