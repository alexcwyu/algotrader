package com.unisoft.algotrader.provider.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.google.common.collect.Lists;
import com.unisoft.algotrader.model.event.bus.MarketDataEventBus;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.provider.ProviderManager;
import com.unisoft.algotrader.provider.data.AbstractDataStoreProvider;
import com.unisoft.algotrader.provider.data.HistoricalSubscriptionKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;

/**
 * Created by alex on 6/18/15.
 */
//TODO batch insert
@Singleton
public class CassandraHistoricalDataStore extends AbstractDataStoreProvider {

    private static final Logger LOG = LogManager.getLogger(CassandraHistoricalDataStore.class);

    private static final String BAR_INSERT_STATEMENT = "INSERT INTO bar (instid, barsize, datetime, open, high, low, close, volume, openint) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String QUOTE_INSERT_STATEMENT = "INSERT INTO quote (instid, datetime, bid, ask, bidsize, asksize) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String TRADE_INSERT_STATEMENT = "INSERT INTO bar (instid, datetime, price, size) VALUES (?, ?, ?, ?);";

    private static final String COL_DATETIME = "datetime";
    private static final String COL_INSTID = "instid";
    private static final String COL_BARSIZE = "barsize";

    private static final String TABLE_BAR = "bar";
    private static final String TABLE_TRADE = "trade";
    private static final String TABLE_QUOTE = "quote";

    public static final String PROVIDER_ID = "Cassandra";

    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final MarketDataEventBus marketDataEventBus;
    private final CassandraHistoricalDataStoreConfig config;
    private Cluster cluster;
    private Session session;

    @Inject
    public CassandraHistoricalDataStore(ProviderManager providerManager, CassandraHistoricalDataStoreConfig config, MarketDataEventBus marketDataEventBus){
        super(providerManager);
        this.config = config;
        this.cluster = Cluster.builder().addContactPoint(config.host).build();
        this.marketDataEventBus = marketDataEventBus;
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

    @Override
    public void connect(){
        if (connected.compareAndSet(false, true)){
            this.session = cluster.connect(config.keyspace);
        }
    }

    @Override
    public void disconnect(){
        if (connected.compareAndSet(true, false)){
            this.cluster.close();
        }
    }

    /// DATASTORE
    @Override
    public void onBar(Bar bar) {
        PreparedStatement statement = session.prepare(BAR_INSERT_STATEMENT);
        BoundStatement boundStatement = new BoundStatement(statement);
        session.execute(boundStatement.bind(bar.instId, bar.size, new Date(bar.dateTime), bar.open, bar.high, bar.low, bar.close, bar.volume, bar.openInt));
    }

    @Override
    public void onQuote(Quote quote) {
        PreparedStatement statement = session.prepare(QUOTE_INSERT_STATEMENT);
        BoundStatement boundStatement = new BoundStatement(statement);
        session.execute(boundStatement.bind(quote.instId, new Date(quote.dateTime), quote.bid, quote.ask, quote.bidSize, quote.askSize));
    }

    @Override
    public void onTrade(Trade trade) {
        PreparedStatement statement = session.prepare(TRADE_INSERT_STATEMENT);
        BoundStatement boundStatement = new BoundStatement(statement);
        session.execute(boundStatement.bind(trade.instId, new Date(trade.dateTime), trade.price, trade.size));
    }

    /// PROVIDER
    @Override
    public boolean subscribeHistoricalData(HistoricalSubscriptionKey subscriptionKey) {
        switch (subscriptionKey.type) {
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
        switch (subscriptionKey.type) {
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


    private void publishBar(HistoricalSubscriptionKey subscriptionKey){
        ResultSet results = queryBar(subscriptionKey);
        for (Row row : results) {
            marketDataEventBus.publishBar(row.getLong(0), row.getInt(1), row.getDate(2).getTime(), row.getDouble(3), row.getDouble(4), row.getDouble(5), row.getDouble(6), row.getLong(7), row.getLong(8));
        }
    }

    private void publishQuote(HistoricalSubscriptionKey subscriptionKey){
        ResultSet results = queryQuote(subscriptionKey);
        for (Row row : results) {
            marketDataEventBus.publishQuote(row.getLong(0), row.getDate(1).getTime(), row.getDouble(2), row.getDouble(3), row.getInt(4), row.getInt(5));
        }
    }

    private void publishTrade(HistoricalSubscriptionKey subscriptionKey){
        ResultSet results = queryTrade(subscriptionKey);
        for (Row row : results) {
            marketDataEventBus.publishTrade(row.getLong(0), row.getDate(1).getTime(), row.getDouble(2), row.getInt(3));
        }
    }

    private List<MarketDataContainer>  loadBar(HistoricalSubscriptionKey subscriptionKey){
        ResultSet results = queryBar(subscriptionKey);
        List<MarketDataContainer> list = Lists.newArrayList();
        for (Row row : results) {
            MarketDataContainer container = new MarketDataContainer();
            container.setBar(row.getLong(0), row.getInt(1), row.getDate(2).getTime(), row.getDouble(3), row.getDouble(4), row.getDouble(5), row.getDouble(6), row.getLong(7), row.getLong(8));
            list.add(container);
        }
        return list;
    }

    private List<MarketDataContainer>  loadQuote(HistoricalSubscriptionKey subscriptionKey){
        ResultSet results = queryQuote(subscriptionKey);
        List<MarketDataContainer> list = Lists.newArrayList();
        for (Row row : results) {
            MarketDataContainer container = new MarketDataContainer();
            container.setQuote(row.getLong(0), row.getDate(1).getTime(), row.getDouble(2), row.getDouble(3), row.getInt(4), row.getInt(5));
            list.add(container);
        }
        return list;
    }

    private List<MarketDataContainer>  loadTrade(HistoricalSubscriptionKey subscriptionKey){
        ResultSet results = queryTrade(subscriptionKey);
        List<MarketDataContainer> list = Lists.newArrayList();
        for (Row row : results) {
            MarketDataContainer container = new MarketDataContainer();
            container.setTrade(row.getLong(0), row.getDate(1).getTime(), row.getDouble(2), row.getInt(3));
            list.add(container);
        }
        return list;
    }


    private ResultSet queryBar(HistoricalSubscriptionKey subscriptionKey){
        Statement select = QueryBuilder.select().all().from(config.keyspace, TABLE_BAR)
                .where(eq(COL_INSTID, subscriptionKey.instId))
                .and(eq(COL_BARSIZE, subscriptionKey.barSize))
                .and(gte(COL_DATETIME, subscriptionKey.fromDate)).and(lt(COL_DATETIME, subscriptionKey.toDate));
        return session.execute(select);
    }

    private ResultSet queryQuote(HistoricalSubscriptionKey subscriptionKey){
        Statement select = QueryBuilder.select().all().from(config.keyspace, TABLE_QUOTE)
                .where(eq(COL_INSTID, subscriptionKey.instId))
                .and(gte(COL_DATETIME, subscriptionKey.fromDate)).and(lt(COL_DATETIME, subscriptionKey.toDate));
        return session.execute(select);
    }

    private ResultSet queryTrade(HistoricalSubscriptionKey subscriptionKey){
        Statement select = QueryBuilder.select().all().from(config.keyspace, TABLE_TRADE)
                .where(eq(COL_INSTID, subscriptionKey.instId))
                .and(gte(COL_DATETIME, subscriptionKey.fromDate)).and(lt(COL_DATETIME, subscriptionKey.toDate));
        return session.execute(select);
    }

}
