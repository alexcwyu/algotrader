package com.unisoft.algotrader.persistence.cassandra;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.unisoft.algotrader.persistence.IdSupplier;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.LongStream;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;

/**
 * Created by alex on 7/14/15.
 */
public class CassandraIdSupplier implements IdSupplier<Long>{

    public static final String COL_TYPE = "entity_class";
    public static final String COL_NEXT_ID = "next_id";

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Session session;
    private final String keySpace;
    private final String type = "default";
    private final String table = "counters";

    public CassandraIdSupplier(Session session, String keySpace) {
        this.session = session;
        this.keySpace = keySpace;
    }


    @Override
    public Long next() {
        return range(1)[0];
    }

    long[] range(long size) {
        lock.writeLock().lock();
        try {
            Statement increment = update(keySpace, table)
                    .with(incr(COL_NEXT_ID, size))
                    .where(eq(COL_TYPE, type))
                    .setConsistencyLevel(ConsistencyLevel.ALL);

            session.execute(increment);

            Long id = latest();

            return LongStream.rangeClosed(id - size + 1, id).toArray();
        } finally {
            lock.writeLock().unlock();
        }
    }

    long latest() {
        lock.readLock().lock();
        try {
            Statement select = select(COL_NEXT_ID)
                    .from(keySpace, table)
                    .where(eq(COL_TYPE, type))
                    .setConsistencyLevel(ConsistencyLevel.ALL);

            long id = session.execute(select).one().getLong(COL_NEXT_ID);

            return id;
        } finally {
            lock.readLock().unlock();
        }
    }
}
