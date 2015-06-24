package com.unisoft.algotrader.provider.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.unisoft.algotrader.event.EventBus;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.provider.SubscriptionKey;
import com.unisoft.algotrader.provider.csv.DataStore;
import com.unisoft.algotrader.provider.csv.historical.HistoricalDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;

/**
 * Created by alex on 6/18/15.
 */
//TODO batch insert
public class CassandraHistoricalDataStore implements DataStore, HistoricalDataProvider {

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

    private final AtomicBoolean connected = new AtomicBoolean(false);

    private final CassandraConfig config;
    private Cluster cluster;
    private Session session;

    public CassandraHistoricalDataStore(CassandraConfig config){
        this.config = config;
        this.cluster = Cluster.builder().addContactPoint(config.host).build();
    }

    /// PROVIDER
    @Override
    public String providerId() {
        return "Cassandra";
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
        Statement select = QueryBuilder.select().all().from(config.keyspace, TABLE_BAR)
                .where(eq(COL_INSTID, subscriptionKey.instId))
                .and(eq(COL_BARSIZE, subscriptionKey.barSize))
                .and(gte(COL_DATETIME, fromDate)).and(lt(COL_DATETIME, toDate));
        ResultSet results = session.execute(select);
        for (Row row : results) {
            eventBus.publishBar(row.getInt(0), row.getInt(1), row.getDate(2).getTime(), row.getDouble(3), row.getDouble(4), row.getDouble(5), row.getDouble(6), row.getLong(7), row.getLong(8));
        }
    }

    private void queryQuote(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, Date fromDate, Date toDate){
        Statement select = QueryBuilder.select().all().from(config.keyspace, TABLE_QUOTE)
                .where(eq(COL_INSTID, subscriptionKey.instId))
                .and(gte(COL_DATETIME, fromDate)).and(lt(COL_DATETIME, toDate));
        ResultSet results = session.execute(select);
        for (Row row : results) {
            eventBus.publishQuote(row.getInt(0), row.getDate(1).getTime(), row.getDouble(2), row.getDouble(3), row.getInt(4), row.getInt(5));
        }
    }

    private void queryTrade(EventBus.MarketDataEventBus eventBus, SubscriptionKey subscriptionKey, Date fromDate, Date toDate){
        Statement select = QueryBuilder.select().all().from(config.keyspace, TABLE_TRADE)
                .where(eq(COL_INSTID, subscriptionKey.instId))
                .and(gte(COL_DATETIME, fromDate)).and(lt(COL_DATETIME, toDate));
        ResultSet results = session.execute(select);
        for (Row row : results) {
            eventBus.publishTrade(row.getInt(0), row.getDate(1).getTime(), row.getDouble(2), row.getInt(3));
        }
    }
}
