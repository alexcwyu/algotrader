package com.unisoft.algotrader.provider.csv;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.unisoft.algotrader.event.EventBus;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.provider.SubscriptionKey;
import com.unisoft.algotrader.provider.csv.historical.HistoricalDataProvider;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.unisoft.algotrader.provider.SubscriptionKey.createSubscriptionKey;
import static com.unisoft.algotrader.provider.csv.CSVConfig.*;

/**
 * Created by alex on 6/16/15.
 */
public class CSVHistoricalDataStore implements DataStore, HistoricalDataProvider{

    private AtomicBoolean connected = new AtomicBoolean(false);
    private final String path;
    private final Writer writer;

    private final LoadingCache<SubscriptionKey, CsvWriter> caches = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<SubscriptionKey, CsvWriter>() {
                        public CsvWriter load(SubscriptionKey key) throws RuntimeException {
                            return createCsvWriter(key);
                        }
                    });

    public CSVHistoricalDataStore(String path){
        this.path = path;
        this.writer = null;
    }

    protected CSVHistoricalDataStore(Writer writer){
        this.writer = writer;
        this.path = null;
    }

    /// PROVIDER
    @Override
    public String providerId() {
        return "CSV";
    }

    @Override
    public boolean connected() {
        return connected.get();
    }

    public void connect(){
        connected.compareAndSet(false, true);
    }

    public void disconnect(){
        connected.compareAndSet(true, false);
        caches.asMap().values().stream().forEach(csvWriter -> csvWriter.close());
    }

    /// DATASTORE
    @Override
    public void onBar(Bar bar) {
        SubscriptionKey key = createSubscriptionKey(bar);
        CsvWriter writer = getOrCreateCsvWriter(key);
        writer.writeRow(bar.dateTime, bar.open, bar.high, bar.low, bar.close, bar.volume, bar.openInt);
    }

    @Override
    public void onQuote(Quote quote) {
        SubscriptionKey key = createSubscriptionKey(quote);
        CsvWriter writer = getOrCreateCsvWriter(key);
        writer.writeRow(quote.dateTime, quote.bid, quote.ask, quote.bidSize, quote.askSize);
    }

    @Override
    public void onTrade(Trade trade) {
        SubscriptionKey key = createSubscriptionKey(trade);
        CsvWriter writer = getOrCreateCsvWriter(key);
        writer.writeRow(trade.dateTime, trade.price, trade.size);
    }

    protected CsvWriter getOrCreateCsvWriter(SubscriptionKey key){
        CsvWriter writer = caches.getUnchecked(key);
        return writer;
    }

    protected CsvWriter createCsvWriter(SubscriptionKey key){
        try {
            CsvWriterSettings settings = new CsvWriterSettings();
            CsvWriter csvWriter = new CsvWriter(getWriter(key), settings);
            csvWriter.writeHeaders(getHeader(key));
            return csvWriter;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    protected Writer getWriter(SubscriptionKey key) {
        try {
            return writer != null? writer : new FileWriter(new File(path + File.separator + getFileName(key)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String [] getHeader(SubscriptionKey key){
        switch (key.type){
            case Bar:
                return BAR_HEADER;
            case Trade:
                return TRADE_HEADER;
            case Quote:
                return QUOTE_HEADER;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public void subscribe(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, Date fromDate, Date toDate) {
        CsvParserSettings settings = new CsvParserSettings();

        settings.getFormat().setLineSeparator("\n");
        settings.getFormat().setDelimiter(',');
        settings.setHeaderExtractionEnabled(true);

        try {
            long fromDateTime = fromDate.getTime();
            long toDateTime = toDate.getTime();
            CsvParser parser = new CsvParser(settings);
            Reader reader = new FileReader(path+CSVConfig.getFileName(subscriptionKey));

            switch (subscriptionKey.type) {
                case Bar:
                    publishBar(eventBus, subscriptionKey, parser, reader, fromDateTime, toDateTime);
                    break;

                case Trade:
                    publishTrade(eventBus, subscriptionKey, parser, reader, fromDateTime, toDateTime);
                    break;

                case Quote:
                    publishQuote(eventBus, subscriptionKey, parser, reader, fromDateTime, toDateTime);
                    break;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void publishBar(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, CsvParser parser, Reader reader, long fromDateTime, long toDateTime) {
        try {
            parser.beginParsing(reader);
            String[] row;
            while ((row = parser.parseNext()) != null) {
                long time = FORMAT.parse(row[0]).getTime();
                if (lt(time, fromDateTime)) continue;
                if (gt(time, toDateTime)) break;
                eventBus.publishBar(subscriptionKey.instId, subscriptionKey.barSize, time,
                        Double.parseDouble(row[1]), Double.parseDouble(row[2]), Double.parseDouble(row[3]), Double.parseDouble(row[4]), Long.parseLong(row[5]), 0);
            }
            parser.stopParsing();

//            List<String[]> allRows = parser.parseAll(reader);
//            for (String[] row : allRows){
//                long time = FORMAT.parse(row[0]).getTime();
//                if (time <fromDateTime)
//                    continue;
//                if (time >toDateTime)
//                    break;
//                eventBus.publishBar(subscriptionKey.instId, subscriptionKey.barSize, time,
//                        Double.parseDouble(row[1]), Double.parseDouble(row[2]), Double.parseDouble(row[3]), Double.parseDouble(row[4]), Long.parseLong(row[5]), 0);
//
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void publishQuote(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, CsvParser parser, Reader reader, long fromDateTime, long toDateTime) {
        try {
            parser.beginParsing(reader);
            String[] row;
            while ((row = parser.parseNext()) != null) {
                long time = FORMAT.parse(row[0]).getTime();
                if (lt(time, fromDateTime)) continue;
                if (gt(time, toDateTime)) break;
                eventBus.publishQuote(subscriptionKey.instId, time,
                        Double.parseDouble(row[1]), Double.parseDouble(row[2]), Integer.parseInt(row[3]), Integer.parseInt(row[4]));
            }
            parser.stopParsing();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void publishTrade(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, CsvParser parser, Reader reader, long fromDateTime, long toDateTime) {
        try {
            parser.beginParsing(reader);
            String[] row;
            while ((row = parser.parseNext()) != null) {
                long time = FORMAT.parse(row[0]).getTime();
                if (lt(time, fromDateTime)) continue;
                if (gt(time, toDateTime)) break;
                eventBus.publishTrade(subscriptionKey.instId, time,
                        Double.parseDouble(row[1]), Integer.parseInt(row[2]));
            }
            parser.stopParsing();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean lt(long time, long fromDateTime){
        return (time < fromDateTime);
    }

    boolean gt(long time, long toDateTime){
        return (time > toDateTime);
    }
}
