package com.unisoft.algotrader.provider.kdb;

import com.exxeleron.qjava.QBasicConnection;
import com.exxeleron.qjava.QConnection;
import com.exxeleron.qjava.QMessage;
import com.exxeleron.qjava.QTable;
import com.google.common.collect.Lists;
import com.unisoft.algotrader.event.bus.LogMarketDataEventBus;
import com.unisoft.algotrader.model.event.bus.MarketDataEventBus;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.provider.ProviderId;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.data.AbstractDataStoreProvider;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import com.unisoft.algotrader.provider.data.SubscriptionKey;
import com.unisoft.algotrader.utils.DateHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by alex on 6/19/15.
 */
@Singleton
public class KDBHistoricalDataStore extends AbstractDataStoreProvider{

    private static final Logger LOG = LogManager.getLogger(KDBHistoricalDataStore.class);

    public static final ProviderId PROVIDER_ID = ProviderId.KDB;

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
    private final MarketDataEventBus marketDataEventBus;
    private final QConnection q;

    @Inject
    public KDBHistoricalDataStore(ProviderManager providerManager, KDBConfig kdbConfig, MarketDataEventBus marketDataEventBus){
        super(providerManager);
        this.kdbConfig = kdbConfig;
        this.marketDataEventBus = marketDataEventBus;
        this.q = new QBasicConnection(kdbConfig.host, kdbConfig.port, kdbConfig.user, kdbConfig.password);
    }

    /// PROVIDER
    @Override
    public ProviderId providerId() {
        return PROVIDER_ID;
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
    public boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        switch (subscriptionKey.subscriptionType.type) {
            case Bar:
                publishBar(subscriptionKey);
                break;

            case Trade:
                publishTrade(subscriptionKey);
                break;

            case Quote:
                publishQuote(subscriptionKey);
                break;
        }

        return true;
    }

    @Override
    public List<MarketDataContainer> loadHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        List<MarketDataContainer> result = Lists.newArrayList();
        switch (subscriptionKey.subscriptionType.type) {
            case Bar:
                result = loadBar(subscriptionKey);
                break;

            case Trade:
                result = loadTrade(subscriptionKey);
                break;

            case Quote:
                result = loadQuote(subscriptionKey);
                break;
        }
        return result;
    }

    private List<MarketDataContainer>  loadBar(HistoricalSubscriptionKey subscriptionKey){
        List<MarketDataContainer> list = Lists.newArrayList();

        QTable table = queryBar(subscriptionKey);
        if (table != null) {
            for (int i = 0; i < table.getRowsCount(); i++) {
                QTable.Row row = table.get(i);
                MarketDataContainer container = new MarketDataContainer();
                container.setBar((int) row.get(0),
                        (int) row.get(1), (long) row.get(2), (double) row.get(3), (double) row.get(4),
                        (double) row.get(5), (double) row.get(6), (int) row.get(7), (int) row.get(8));
                list.add(container);
            }
        }
        return list;
    }
    private List<MarketDataContainer>  loadTrade(HistoricalSubscriptionKey subscriptionKey){
        List<MarketDataContainer> list = Lists.newArrayList();

        QTable table = queryTrade(subscriptionKey);
        if (table != null) {
            for (int i = 0; i < table.getRowsCount(); i++) {
                QTable.Row row = table.get(i);
                MarketDataContainer container = new MarketDataContainer();
                container.setTrade((int) row.get(0),
                        (long) row.get(1), (double) row.get(2), (int) row.get(3));
                list.add(container);
            }
        }
        return list;
    }

    private List<MarketDataContainer>  loadQuote(HistoricalSubscriptionKey subscriptionKey){
        List<MarketDataContainer> list = Lists.newArrayList();

        QTable table = queryQuote(subscriptionKey);
        if (table != null) {
            for (int i = 0; i < table.getRowsCount(); i++) {
                QTable.Row row = table.get(i);
                MarketDataContainer container = new MarketDataContainer();
                container.setQuote((int) row.get(0),
                        (long) row.get(1), (double) row.get(2), (double) row.get(3),
                        (int) row.get(4), (int) row.get(5));
                list.add(container);
            }
        }
        return list;
    }

    private void publishBar(HistoricalSubscriptionKey subscriptionKey) {
        QTable table = queryBar(subscriptionKey);
        if (table != null) {
            for (int i = 0; i < table.getRowsCount(); i++) {
                QTable.Row row = table.get(i);
                marketDataEventBus.publishBar((int) row.get(0),
                        (int) row.get(1), (long) row.get(2), (double) row.get(3), (double) row.get(4),
                        (double) row.get(5), (double) row.get(6), (int) row.get(7), (int) row.get(8));
            }
        }
    }

    private void publishQuote(HistoricalSubscriptionKey subscriptionKey) {
        QTable table = queryQuote(subscriptionKey);

        if (table != null) {
            for (int i = 0; i < table.getRowsCount(); i++) {
                QTable.Row row = table.get(i);
                marketDataEventBus.publishQuote((int) row.get(0),
                        (long) row.get(1), (double) row.get(2), (double) row.get(3),
                        (int) row.get(4), (int) row.get(5));
            }
        }
    }

    private void publishTrade(HistoricalSubscriptionKey subscriptionKey) {
        QTable table = queryTrade(subscriptionKey);
        if (table != null) {
            for (int i = 0; i < table.getRowsCount(); i++) {
                QTable.Row row = table.get(i);
                marketDataEventBus.publishTrade((int) row.get(0),
                        (long) row.get(1), (double) row.get(2), (int) row.get(3));
            }
        }
    }

    private QTable queryBar(HistoricalSubscriptionKey subscriptionKey){
        try {
            String query = buildBarSelectQuery(subscriptionKey, subscriptionKey.fromDate, subscriptionKey.toDate);
            q.query(QConnection.MessageType.SYNC, query);
            final QMessage message = (QMessage) q.receive(false, false);

            if (message.getData() !=null && message.getData() instanceof  QTable) {
                return (QTable) message.getData();
            }
            else{
                LOG.warn("fail to query bar");
            }
        }
        catch (Exception e){
            LOG.error("Fail to query bar", e);
        }
        return null;
    }

    private QTable queryQuote(HistoricalSubscriptionKey subscriptionKey){
        try {
            String query = buildQuoteSelectQuery(subscriptionKey, subscriptionKey.fromDate, subscriptionKey.toDate);
            q.query(QConnection.MessageType.SYNC, query);
            final QMessage message = (QMessage) q.receive(false, false);

            if (message.getData() !=null && message.getData() instanceof  QTable) {

                return (QTable) message.getData();
            }
            else{
                LOG.warn("fail to query quote");
            }
        }
        catch (Exception e){
            LOG.error("Fail to query quote", e);
        }
        return null;
    }

    private QTable queryTrade(HistoricalSubscriptionKey subscriptionKey){
        try {
            String query = buildTradeSelectQuery(subscriptionKey, subscriptionKey.fromDate, subscriptionKey.toDate);
            q.query(QConnection.MessageType.SYNC, query);
            final QMessage message = (QMessage) q.receive(false, false);

            if (message.getData() !=null && message.getData() instanceof  QTable) {
                return (QTable) message.getData();
            }
            else {
                LOG.warn("fail to query trade");
            }
        }
        catch (Exception e){
            LOG.error("Fail to query trade", e);
        }
        return null;
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

    public static String buildBarSelectQuery(SubscriptionKey subscriptionKey, long fromDate, long toDate){
        return new StringBuilder(BAR_SELECT_PREFIX).append(subscriptionKey.instId)
                .append(SIZE_EQ).append(subscriptionKey.subscriptionType.barSize)
                .append(DT_GE).append(fromDate)
                .append(DT_LT).append(toDate).toString();
    }

    public static String buildTradeSelectQuery(SubscriptionKey subscriptionKey, long fromDate, long toDate){
        return new StringBuilder(TRADE_SELECT_PREFIX).append(subscriptionKey.instId)
                .append(DT_GE).append(fromDate)
                .append(DT_LT).append(toDate).toString();
    }

    public static String buildQuoteSelectQuery(SubscriptionKey subscriptionKey, long fromDate, long toDate){
        return new StringBuilder(QUOTE_SELECT_PREFIX).append(subscriptionKey.instId)
                .append(DT_GE).append(fromDate)
                .append(DT_LT).append(toDate).toString();
    }

    public static void main(String [] args) throws Exception{

        ProviderManager providerManager = new ProviderManager();
        KDBHistoricalDataStore store = new KDBHistoricalDataStore(providerManager, new KDBConfig("127.0.0.1", 5000, null, null), new LogMarketDataEventBus());
        store.connect();
        long instId =100;
        for (int i = 0 ; i < 10; i ++) {
            long time = DateHelper.fromYYYYMMDD(20000101 + i).getTime();
            Bar bar = new Bar(instId, 86400, time,88, 98, 86, 92, 20000);
            Quote quote = new Quote(instId, time, 98, 100, 9800, 8800);
            Trade trade = new Trade(instId, time, 99, 8800);

            store.onBar(bar);
            store.onQuote(quote);
            store.onTrade(trade);
        }

        store.subscribeHistoricalData(HistoricalSubscriptionKey.createDailySubscriptionKey(PROVIDER_ID.id, instId, 20000101, 20000112));
        store.subscribeHistoricalData(HistoricalSubscriptionKey.createQuoteSubscriptionKey(PROVIDER_ID.id, instId, 20000101, 20000112));
        store.subscribeHistoricalData(HistoricalSubscriptionKey.createTradeSubscriptionKey(PROVIDER_ID.id, instId, 20000101, 20000112));

    }
}
