package com.unisoft.algotrader.provider.csv;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.event.EventBus;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.data.*;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.unisoft.algotrader.provider.csv.CSVUtils.*;
import static com.unisoft.algotrader.provider.data.SubscriptionKey.createSubscriptionKey;

/**
 * Created by alex on 6/16/15.
 */
@Singleton
public class CSVHistoricalDataStore implements DataStoreProvider, HistoricalDataProvider{

    private AtomicBoolean connected = new AtomicBoolean(false);
    private final CSVConfig config;
    private final Writer writer;
    private final RefDataStore refDataStore;
    public static final String PROVIDER_ID = "CSV";
    private CsvParserSettings settings = new CsvParserSettings();

    private final LoadingCache<SubscriptionKey, CsvWriter> caches = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<SubscriptionKey, CsvWriter>() {
                        public CsvWriter load(SubscriptionKey key) throws RuntimeException {
                            return createCsvWriter(key);
                        }
                    });

    @Inject
    public CSVHistoricalDataStore(CSVConfig csvConfig, RefDataStore refDataStore){
        this.config = csvConfig;
        this.writer = null;
        this.refDataStore = refDataStore;

        init();

    }

    protected CSVHistoricalDataStore(Writer writer, RefDataStore refDataStore){
        this.writer = writer;
        this.config = null;
        this.refDataStore = refDataStore;
        init();
    }

    private void init(){


        settings.getFormat().setLineSeparator("\n");
        settings.getFormat().setDelimiter(',');
        settings.setHeaderExtractionEnabled(true);
    }

    /// PROVIDER
    @Override
    public String providerId() {
        return PROVIDER_ID;
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
        SubscriptionKey key = createSubscriptionKey(PROVIDER_ID, bar);
        CsvWriter writer = getOrCreateCsvWriter(key);
        writer.writeRow(bar.dateTime, bar.open, bar.high, bar.low, bar.close, bar.volume, bar.openInt);
    }

    @Override
    public void onQuote(Quote quote) {
        SubscriptionKey key = createSubscriptionKey(PROVIDER_ID, quote);
        CsvWriter writer = getOrCreateCsvWriter(key);
        writer.writeRow(quote.dateTime, quote.bid, quote.ask, quote.bidSize, quote.askSize);
    }

    @Override
    public void onTrade(Trade trade) {
        SubscriptionKey key = createSubscriptionKey(PROVIDER_ID, trade);
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
            Instrument instrument = refDataStore.getInstrument(key.instId);
            return writer != null? writer : new FileWriter(new File(config.path + File.separator + getFileName(key, instrument.getSymbol())));
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

    private Reader getReader(HistoricalSubscriptionKey subscriptionKey) {
        try {

            Instrument instrument = refDataStore.getInstrument(subscriptionKey.instId);
            return new FileReader(config.path + CSVUtils.getFileName(subscriptionKey, instrument.getSymbol()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey, Subscriber subscriber) {

        long fromDateTime = subscriptionKey.fromDate;
        long toDateTime = subscriptionKey.toDate;
        CsvParser parser = new CsvParser(settings);

        Reader reader = getReader(subscriptionKey);

        switch (subscriptionKey.type) {
            case Bar:
                publishBar(subscriber.marketDataEventBus, subscriptionKey, parser, reader, fromDateTime, toDateTime);
                break;

            case Trade:
                publishTrade(subscriber.marketDataEventBus, subscriptionKey, parser, reader, fromDateTime, toDateTime);
                break;

            case Quote:
                publishQuote(subscriber.marketDataEventBus, subscriptionKey, parser, reader, fromDateTime, toDateTime);
                break;
        }
        return true;
    }

    @Override
    public List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        long fromDateTime = subscriptionKey.fromDate;
        long toDateTime = subscriptionKey.toDate;
        CsvParser parser = new CsvParser(settings);

        Reader reader = getReader(subscriptionKey);

        List<MarketDataContainer> result = Lists.newArrayList();
        switch (subscriptionKey.type) {
            case Bar:
                result = loadBar(subscriptionKey, parser, reader, fromDateTime, toDateTime);
                break;

            case Trade:
                result = loadTrade(subscriptionKey, parser, reader, fromDateTime, toDateTime);
                break;

            case Quote:
                result = loadQuote(subscriptionKey, parser, reader, fromDateTime, toDateTime);
                break;
        }
        return result;
    }

    private List<MarketDataContainer> loadBar(HistoricalSubscriptionKey subscriptionKey, CsvParser parser, Reader reader, long fromDateTime, long toDateTime){

        try {
            List<MarketDataContainer> list = Lists.newArrayList();
            List<String[]> allRows = parser.parseAll(reader);
            for (String[] row : allRows) {
                long time = FORMAT.parse(row[0]).getTime();
                if (time < fromDateTime)
                    continue;
                if (time > toDateTime)
                    break;

                MarketDataContainer container = new MarketDataContainer();
                container.setBar(subscriptionKey.instId, subscriptionKey.barSize, time,
                        Double.parseDouble(row[1]), Double.parseDouble(row[2]), Double.parseDouble(row[3]), Double.parseDouble(row[4]), Long.parseLong(row[5]), 0);

                list.add(container);
            }
            return list;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private List<MarketDataContainer> loadQuote(HistoricalSubscriptionKey subscriptionKey, CsvParser parser, Reader reader, long fromDateTime, long toDateTime){

        try {
            List<MarketDataContainer> list = Lists.newArrayList();
            List<String[]> allRows = parser.parseAll(reader);
            for (String[] row : allRows) {
                long time = FORMAT.parse(row[0]).getTime();
                if (time < fromDateTime)
                    continue;
                if (time > toDateTime)
                    break;

                MarketDataContainer container = new MarketDataContainer();
                container.setQuote(subscriptionKey.instId, time,
                        Double.parseDouble(row[1]), Double.parseDouble(row[2]), Integer.parseInt(row[3]), Integer.parseInt(row[4]));

                list.add(container);
            }
            return list;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    private List<MarketDataContainer> loadTrade(HistoricalSubscriptionKey subscriptionKey, CsvParser parser, Reader reader, long fromDateTime, long toDateTime){

        try {
            List<MarketDataContainer> list = Lists.newArrayList();
            List<String[]> allRows = parser.parseAll(reader);
            for (String[] row : allRows) {
                long time = FORMAT.parse(row[0]).getTime();
                if (time < fromDateTime)
                    continue;
                if (time > toDateTime)
                    break;

                MarketDataContainer container = new MarketDataContainer();
                container.setTrade(subscriptionKey.instId, time,
                        Double.parseDouble(row[1]), Integer.parseInt(row[2]));

                list.add(container);
            }
            return list;
        }
        catch (Exception e){
            throw new RuntimeException(e);
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
//                eventBus.publishBar(subscriptionKey.altInstId, subscriptionKey.barSize, time,
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
