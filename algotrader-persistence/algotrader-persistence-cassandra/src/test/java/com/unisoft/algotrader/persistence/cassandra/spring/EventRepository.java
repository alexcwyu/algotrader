package com.unisoft.algotrader.persistence.cassandra.spring;

/**
 * Created by alex on 6/28/15.
 */
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

public interface EventRepository extends CassandraRepository<Event> {

    @Query("select * from event where type = ?0 and bucket=?1")
    Iterable<Event> findByTypeAndBucket(String type, String bucket);
}