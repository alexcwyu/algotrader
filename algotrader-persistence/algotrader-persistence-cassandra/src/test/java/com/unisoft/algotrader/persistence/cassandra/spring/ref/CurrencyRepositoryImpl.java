package com.unisoft.algotrader.persistence.cassandra.spring.ref;

import com.datastax.driver.core.*;
import com.unisoft.algotrader.core.Currency;
import org.springframework.data.cassandra.core.CassandraOperations;

import javax.inject.Inject;

import static com.datastax.driver.core.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;
import static com.datastax.driver.core.querybuilder.QueryBuilder.ttl;
import static com.datastax.driver.core.querybuilder.QueryBuilder.update;

import com.datastax.driver.core.Session;

/**
 * Created by alex on 6/30/15.
 */
public class CurrencyRepositoryImpl implements CurrencyRepositoryCustom {

    public static final String CCY_ID = "ccy_id";
    public static final String NAME = "name";
    public static final String BUS_TIME = "bus_time";
    public static final String SYS_TIME = "sys_time";

    public static final String TABLE  = "currency";

//
//    private RegularStatement insertStatement() {
//        return insertInto(TABLE)
//                .value(CCY_ID, bindMarker())
//                .value(NAME, bindMarker());
//    }

    RegularStatement insertStatement = insertInto(TABLE)
            .value(CCY_ID, bindMarker())
            .value(NAME, bindMarker());
    PreparedStatement insertPreparedStatement;

    private CassandraOperations cassandraOperations;

    @Inject
    CurrencyRepositoryImpl(CassandraOperations cassandraOperations) {
        this.cassandraOperations = cassandraOperations;
        Session session = cassandraOperations.getSession();
        insertPreparedStatement = session.prepare(insertStatement);
    }


    private BoundStatement bindInsert(Currency currency){
        BoundStatement boundStatement = insertPreparedStatement.bind(currency.getCcyId(), currency.getName());
        return boundStatement;
    }

    public Currency save(Currency currency){
        BoundStatement boundStatement = bindInsert(currency);
        cassandraOperations.execute(boundStatement);
        return currency;
    }

    public Iterable<Currency> save(Iterable<Currency> currencies){
        BatchStatement batch = new BatchStatement();
        for (Currency currency : currencies){
            batch.add(bindInsert(currency));
        }
        cassandraOperations.execute(batch);

        return currencies;
    }

    public Currency save(Currency currency, long busTime){
        return save(currency);
    }
}
