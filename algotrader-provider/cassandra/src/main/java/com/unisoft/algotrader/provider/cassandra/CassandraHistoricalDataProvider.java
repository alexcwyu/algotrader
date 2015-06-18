package com.unisoft.algotrader.provider.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.unisoft.algotrader.core.id.InstId;
import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.provider.SubscriptionKey;
import com.unisoft.algotrader.provider.csv.historical.HistoricalDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;

/**
 * Created by alex on 6/18/15.
 */
public class CassandraHistoricalDataProvider implements HistoricalDataProvider {

    private static final Logger LOG = LogManager.getLogger(CassandraHistoricalDataProvider.class);


    private AtomicBoolean connected = new AtomicBoolean(false);

    private CassandraConfig config;
    private Cluster cluster;
    private Session session;

    public CassandraHistoricalDataProvider(CassandraConfig config){
        this.config = config;
        this.cluster = Cluster.builder().addContactPoint(config.host).build();
    }

    @Override
    public void subscribe(SubscriptionKey subscriptionKey, Date fromDate, Date toDate) {
        switch (subscriptionKey.type) {
            case Bar:
                queryBar(subscriptionKey, fromDate, toDate);
                break;

            case Trade:
                queryTrade(subscriptionKey, fromDate, toDate);
                break;

            case Quote:
                queryQuote(subscriptionKey, fromDate, toDate);
                break;
        }
    }


    private void queryBar(SubscriptionKey subscriptionKey, Date fromDate, Date toDate){
        Statement select = QueryBuilder.select().all().from(config.keyspace, "bar")
                .where(eq("instid", subscriptionKey.instId.toString()))
                .and(eq("barsize", subscriptionKey.barSize))
                .and(gte("datetime", fromDate)).and(lt("datetime", toDate));
        ResultSet results = session.execute(select);
        for (Row row : results) {
            Bar bar = new Bar(InstId.parse(row.getString(0)), row.getDate(2).getTime(), row.getInt(1), row.getDouble(4), row.getDouble(5), row.getDouble(3), row.getDouble(6), row.getLong(7), row.getLong(8));
            System.out.println(bar);
        }
    }

    private void queryQuote(SubscriptionKey subscriptionKey, Date fromDate, Date toDate){
        Statement select = QueryBuilder.select().all().from(config.keyspace, "quote")
                .where(eq("instid", subscriptionKey.instId))
                .and(gte("datetime", fromDate)).and(lt("datetime", toDate));
        ResultSet results = session.execute(select);
        for (Row row : results) {
            Quote quote = new Quote(InstId.parse(row.getString(0)), row.getDate(1).getTime(), row.getDouble(2), row.getDouble(3), row.getInt(4), row.getInt(5));
            LOG.info(quote);
        }
    }

    private void queryTrade(SubscriptionKey subscriptionKey, Date fromDate, Date toDate){
        Statement select = QueryBuilder.select().all().from(config.keyspace, "trade")
                .where(eq("instid", subscriptionKey.instId))
                .and(gte("datetime", fromDate)).and(lt("datetime", toDate));
        ResultSet results = session.execute(select);
        for (Row row : results) {
            Trade trade = new Trade(InstId.parse(row.getString(0)), row.getDate(1).getTime(), row.getDouble(2), row.getInt(3));
            LOG.info(trade);
        }
    }
    @Override
    public String providerId() {
        return "Cassandra";
    }

    @Override
    public boolean connected() {
        return connected.get();
    }


    public void connect(){
        if (connected.compareAndSet(false, true)){
            this.session = cluster.connect(config.keyspace);
        }

    }

    public void disconnect(){
        if (connected.compareAndSet(true, false)){
            this.cluster.close();
        }

    }

    public static void main(String [] args) throws Exception{
        CassandraHistoricalDataProvider provider = new CassandraHistoricalDataProvider(new CassandraConfig());
        provider.connect();
        provider.subscribe(SubscriptionKey.createBarSubscriptionKey(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), 60), YYYYMMDD_FORMAT.parse("20101010"), YYYYMMDD_FORMAT.parse("20161010"));
    }
}
