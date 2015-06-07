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
public class CSVHistoricalDataProvider  implements HistoricalDataProvider{

    private final String path;
    private final RingBuffer<MarketDataContainer> marketDataRB;

    private final static  int DAILY_SIZE = 60*60*24;
    public CSVHistoricalDataProvider(String path){
        this(path, EventBusManager.INSTANCE.rawMarketDataRB);
    }
    public CSVHistoricalDataProvider(String path, RingBuffer<MarketDataContainer> marketDataRB){
        this.path = path;
        this.marketDataRB = marketDataRB;
    }

    public static SimpleDateFormat FORMAT= new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat FORMAT2= new SimpleDateFormat("yyyyMMdd");
    @Override
    public void subscribe(String instId, int fromDate, int toDate) {
        CsvParserSettings settings = new CsvParserSettings();

        settings.getFormat().setLineSeparator("\n");
        settings.getFormat().setDelimiter(',');
        settings.setHeaderExtractionEnabled(true);

        try {

            long fromDateTime = FORMAT2.parse(Integer.toString(fromDate)).getTime();
            long toDateTime = FORMAT2.parse(Integer.toString(toDate)).getTime();
            String[] row;
            CsvParser parser = new CsvParser(settings);
            Reader reader = new FileReader(path+instId+".csv");
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
                event.instId = instId;
                event.bitset.set(MarketDataContainer.BAR_BIT);

                Bar bar = event.bar;
                bar.instId = instId;
                bar.dateTime = time;
                bar.size = DAILY_SIZE;
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

    public static void main(String [] args){
        CSVHistoricalDataProvider provider = new CSVHistoricalDataProvider("/mnt/data/dev/workspaces/algo/algotrader3/src/main/resources/");

        provider.subscribe("HSI", 20110101, 20141231);
    }
}
