package com.unisoft.algotrader.provider.kdb;

import com.exxeleron.qjava.QBasicConnection;
import com.exxeleron.qjava.QConnection;
import com.exxeleron.qjava.QMessage;
import com.exxeleron.qjava.QTable;
import com.unisoft.algotrader.event.LogMarketDataEventBus;
import com.unisoft.algotrader.model.event.EventBus;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.provider.DataStore;
import com.unisoft.algotrader.provider.SubscriptionKey;
import com.unisoft.algotrader.provider.historical.HistoricalDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by alex on 6/19/15.
 */
public class KDBHistoricalDataStore implements DataStore, HistoricalDataProvider {

    private static final Logger LOG = LogManager.getLogger(KDBHistoricalDataStore.class);

    private static final String BAR_INSERT_PREFIX = "`bar insert (`";
    private static final String QUOTE_INSERT_PREFIX = "`quote insert (`";
    private static final String TRADE_INSERT_PREFIX = "`trade insert (`";
    private static final String FLOAT_FIELD = "e";
    private static final String INSERT_FIELD_SEP = ";";
    private static final String INSERT_SUFFIX = ")";

    private static final String BAR_SELECT_PREFIX = "select[<datetime] from bar where sym=`";
    private static final String QUOTE_SELECT_PREFIX = "select[<datetime] from quote where sym=`";
    private static final String TRADE_SELECT_PREFIX = "select[<datetime] from trade where sym=`";
    private static final String SIZE_EQ = ", size = ";
    private static final String DT_GE = ", datetime >= ";
    private static final String DT_LT = ", datetime < ";


    private final AtomicBoolean connected = new AtomicBoolean(false);

    private final KDBConfig kdbConfig;
    private final QConnection q;

    public KDBHistoricalDataStore(KDBConfig kdbConfig){
        this.kdbConfig = kdbConfig;
        this.q = new QBasicConnection(kdbConfig.host, kdbConfig.port, kdbConfig.username, kdbConfig.password);
    }

    /// PROVIDER
    @Override
    public String providerId() {
        return "KDB";
    }

    @Override
    public boolean connected() {
        return connected.get();
    }

    @Override
    public void connect(){
        if (connected.compareAndSet(false, true)){
            //connect
            try {
                q.open();
            }
            catch (Exception e){
                connected.set(false);
                LOG.error("Fail to open connection", e);
            }
        }
    }

    @Override
    public void disconnect(){
        if (connected.compareAndSet(true, false)){
            //close
            try {
                q.close();
            }
            catch (Exception e){
                connected.set(true);
                LOG.error("Fail to open connection", e);
            }
        }
    }

    /// DATASTORE
    @Override
    public void onBar(Bar bar) {
        try {
            String query = buildBarInsertQuery(bar);
            q.async(query);
        }
        catch (Exception e){
            LOG.error("Fail to insert bar", e);
        }

    }

    @Override
    public void onQuote(Quote quote) {
        try {
            String query = buildQuoteInsertQuery(quote);
            q.async(query);
        } catch (Exception e) {
            LOG.error("Fail to insert quote", e);
        }
    }

    @Override
    public void onTrade(Trade trade) {
        try {
            String query = buildTradeInsertQuery(trade);
            q.async(query);
        } catch (Exception e) {
            LOG.error("Fail to insert trade", e);
        }
    }

    /// PROVIDER
    @Override
    public void subscribe(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, Date fromDate, Date toDate) {
        switch (subscriptionKey.type) {
            case Bar:
                queryBar(eventBus, subscriptionKey, fromDate, toDate);
                break;

            case Trade:
                queryTrade(eventBus, subscriptionKey, fromDate, toDate);
                break;

            case Quote:
                queryQuote(eventBus, subscriptionKey, fromDate, toDate);
                break;
        }
    }

    private void queryBar(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, Date fromDate, Date toDate){
        try {
            String query = buildBarSelectQuery(subscriptionKey, fromDate, toDate);
            q.query(QConnection.MessageType.SYNC, query);
            final QMessage message = (QMessage) q.receive(false, false);

            if (message.getData() !=null && message.getData() instanceof  QTable) {
                QTable table = (QTable) message.getData();
                for (int i = 0; i < table.getRowsCount(); i++) {
                    QTable.Row row = table.get(i);

                    eventBus.publishBar((int)row.get(0),
                            (int) row.get(1), (long) row.get(2), (double) row.get(3), (double) row.get(4),
                            (double) row.get(5), (double) row.get(6), (int) row.get(7), (int) row.get(8));
                }
            }
            else{
                LOG.warn("fail to query bar");
            }
        }
        catch (Exception e){
            LOG.error("Fail to query bar", e);
        }
    }

    private void queryQuote(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, Date fromDate, Date toDate){
        try {
            String query = buildQuoteSelectQuery(subscriptionKey, fromDate, toDate);
            q.query(QConnection.MessageType.SYNC, query);
            final QMessage message = (QMessage) q.receive(false, false);

            if (message.getData() !=null && message.getData() instanceof  QTable) {
                QTable table = (QTable) message.getData();
                for (int i = 0; i < table.getRowsCount(); i++) {
                    QTable.Row row = table.get(i);

                    eventBus.publishQuote((int)row.get(0),
                            (long) row.get(1), (double) row.get(2), (double) row.get(3),
                            (int) row.get(4), (int) row.get(5));
                }
            }
            else{
                LOG.warn("fail to query quote");
            }
        }
        catch (Exception e){
            LOG.error("Fail to query quote", e);
        }
    }

    private void queryTrade(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, Date fromDate, Date toDate){
        try {
            String query = buildTradeSelectQuery(subscriptionKey, fromDate, toDate);
            q.query(QConnection.MessageType.SYNC, query);
            final QMessage message = (QMessage) q.receive(false, false);

            if (message.getData() !=null && message.getData() instanceof  QTable) {
                QTable table = (QTable) message.getData();
                for (int i = 0; i < table.getRowsCount(); i++) {
                    QTable.Row row = table.get(i);

                    eventBus.publishTrade((int)row.get(0),
                            (long) row.get(1), (double) row.get(2), (int) row.get(3));
                }
            }
            else {
                LOG.warn("fail to query trade");
            }
        }
        catch (Exception e){
            LOG.error("Fail to query trade", e);
        }
    }

    public static String buildBarInsertQuery(Bar bar){
        return new StringBuilder(BAR_INSERT_PREFIX).append(bar.instId).append(INSERT_FIELD_SEP)
                .append(bar.size).append(INSERT_FIELD_SEP)
                .append(bar.dateTime).append(INSERT_FIELD_SEP)
                .append(bar.open).append(FLOAT_FIELD).append(INSERT_FIELD_SEP)
                .append(bar.high).append(FLOAT_FIELD).append(INSERT_FIELD_SEP)
                .append(bar.low).append(FLOAT_FIELD).append(INSERT_FIELD_SEP)
                .append(bar.close).append(FLOAT_FIELD).append(INSERT_FIELD_SEP)
                .append(bar.volume).append(INSERT_FIELD_SEP)
                .append(bar.openInt).append(INSERT_SUFFIX).toString();
    }


    public static String buildTradeInsertQuery(Trade trade){
        return new StringBuilder(TRADE_INSERT_PREFIX).append(trade.instId).append(INSERT_FIELD_SEP)
                .append(trade.dateTime).append(INSERT_FIELD_SEP)
                .append(trade.price).append(FLOAT_FIELD).append(INSERT_FIELD_SEP)
                .append(trade.size).append(INSERT_SUFFIX).toString();
    }

    public static String buildQuoteInsertQuery(Quote quote){
        return new StringBuilder(QUOTE_INSERT_PREFIX).append(quote.instId).append(INSERT_FIELD_SEP)
                .append(quote.dateTime).append(INSERT_FIELD_SEP)
                .append(quote.bid).append(FLOAT_FIELD).append(INSERT_FIELD_SEP)
                .append(quote.ask).append(FLOAT_FIELD).append(INSERT_FIELD_SEP)
                .append(quote.bidSize).append(INSERT_FIELD_SEP)
                .append(quote.askSize).append(INSERT_SUFFIX).toString();
    }

    public static String buildBarSelectQuery(SubscriptionKey subscriptionKey, Date fromDate, Date toDate){
        return new StringBuilder(BAR_SELECT_PREFIX).append(subscriptionKey.instId)
                .append(SIZE_EQ).append(subscriptionKey.barSize)
                .append(DT_GE).append(fromDate.getTime())
                .append(DT_LT).append(toDate.getTime()).toString();
    }

    public static String buildTradeSelectQuery(SubscriptionKey subscriptionKey, Date fromDate, Date toDate){
        return new StringBuilder(TRADE_SELECT_PREFIX).append(subscriptionKey.instId)
                .append(DT_GE).append(fromDate.getTime())
                .append(DT_LT).append(toDate.getTime()).toString();
    }

    public static String buildQuoteSelectQuery(SubscriptionKey subscriptionKey, Date fromDate, Date toDate){
        return new StringBuilder(QUOTE_SELECT_PREFIX).append(subscriptionKey.instId)
                .append(DT_GE).append(fromDate.getTime())
                .append(DT_LT).append(toDate.getTime()).toString();
    }

    public static void main(String [] args) throws Exception{

        KDBHistoricalDataStore store = new KDBHistoricalDataStore(new KDBConfig());
        store.connect();
        long instId =100;
        for (int i = 0 ; i < 10; i ++) {
            long time = YYYYMMDD_FORMAT.parse((20000101 + i)+"").getTime();
            Bar bar = new Bar(instId, 86400, time,88, 98, 86, 92, 20000);
            Quote quote = new Quote(instId, time, 98, 100, 9800, 8800);
            Trade trade = new Trade(instId, time, 99, 8800);

            store.onBar(bar);
            store.onQuote(quote);
            store.onTrade(trade);
        }

        LogMarketDataEventBus logger = new LogMarketDataEventBus();
        store.subscribe(logger, SubscriptionKey.createDailySubscriptionKey(instId), 20000101, 20000112);
        store.subscribe(logger, SubscriptionKey.createQuoteSubscriptionKey(instId), 20000101, 20000112);
        store.subscribe(logger, SubscriptionKey.createTradeSubscriptionKey(instId), 20000101, 20000112);

    }
}
