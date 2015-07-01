package com.unisoft.algotrader.persistence.cassandra.spring.ref;

import com.datastax.driver.core.*;
import com.unisoft.algotrader.core.Exchange;
import org.springframework.data.cassandra.core.CassandraOperations;

import javax.inject.Inject;

import static com.datastax.driver.core.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;

/**
 * Created by alex on 6/30/15.
 */
public class ExchangeRepositoryImpl implements ExchangeRepositoryCustom {

    public static final String EXCH_ID = "exch_id";
    public static final String NAME = "name";
    public static final String BUS_TIME = "bus_time";
    public static final String SYS_TIME = "sys_time";

    public static final String TABLE  = "exchange";

//
//    private RegularStatement insertStatement() {
//        return insertInto(TABLE)
//                .value(CCY_ID, bindMarker())
//                .value(NAME, bindMarker());
//    }

    RegularStatement insertStatement = insertInto(TABLE)
            .value(EXCH_ID, bindMarker())
            .value(NAME, bindMarker());
    PreparedStatement insertPreparedStatement;

    private CassandraOperations cassandraOperations;

    @Inject
    ExchangeRepositoryImpl(CassandraOperations cassandraOperations) {
        this.cassandraOperations = cassandraOperations;
        Session session = cassandraOperations.getSession();
        insertPreparedStatement = session.prepare(insertStatement);
    }


    private BoundStatement bindInsert(Exchange exchange){
        BoundStatement boundStatement = insertPreparedStatement.bind(exchange.getExchId(), exchange.getName());
        return boundStatement;
    }

    public Exchange save(Exchange exchange){
        BoundStatement boundStatement = bindInsert(exchange);
        cassandraOperations.execute(boundStatement);
        return exchange;
    }

    public Iterable<Exchange> save(Iterable<Exchange> exchanges){
        BatchStatement batch = new BatchStatement();
        for (Exchange exchange : exchanges){
            batch.add(bindInsert(exchange));
        }
        cassandraOperations.execute(batch);

        return exchanges;
    }

    public Exchange save(Exchange exchange, long busTime){
        return save(exchange);
    }
}
