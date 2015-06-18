package com.unisoft.algotrader.provider.csv;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.provider.SubscriptionKey;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.unisoft.algotrader.provider.SubscriptionKey.createSubscriptionKey;
import static com.unisoft.algotrader.provider.csv.CSVConfig.*;

/**
 * Created by alex on 6/16/15.
 */
public class CSVDataStore implements DataStore {

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

    public CSVDataStore(String path){
        this.path = path;
        this.writer = null;
    }

    protected CSVDataStore(Writer writer){
        this.writer = writer;
        this.path = null;
    }


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


    @Override
    public void onBar(Bar bar) {
        SubscriptionKey key = createSubscriptionKey(bar);
        CsvWriter writer = getOrCreateCsvWriter(key);
        //String dateStr = formatDate(bar.dateTime);
        writer.writeRow(bar.dateTime, bar.open, bar.high, bar.low, bar.close, bar.volume, bar.openInt);
    }

    @Override
    public void onQuote(Quote quote) {
        SubscriptionKey key = createSubscriptionKey(quote);
        CsvWriter writer = getOrCreateCsvWriter(key);
        //String dateStr = formatDate(quote.dateTime);
        writer.writeRow(quote.dateTime, quote.bid, quote.ask, quote.bidSize, quote.askSize);
    }

    @Override
    public void onTrade(Trade trade) {
        SubscriptionKey key = createSubscriptionKey(trade);
        CsvWriter writer = getOrCreateCsvWriter(key);
        //String dateStr = formatDate(trade.dateTime);
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
}
