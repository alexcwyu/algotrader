package com.unisoft.algotrader.persistence.cassandra.spring;

/**
 * Created by alex on 6/28/15.
 */
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.utils.UUIDs;
import com.google.common.collect.ImmutableSet;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CassandraTemplateIntegrationTest extends BaseIntegrationTest {

    public static final String TIME_BUCKET = "2014-01-01";

    @Autowired
    private CassandraOperations cassandraTemplate;

    @Test
    public void supportsPojoToCqlMappings() {
        Event event = new Event(UUIDs.timeBased(), "type1", TIME_BUCKET, ImmutableSet.of("tag1", "tag3"));
        cassandraTemplate.insert(event);

        Select select = QueryBuilder.select().from("event").where(QueryBuilder.eq("type", "type1")).and(QueryBuilder.eq("bucket", TIME_BUCKET)).limit(10);

        Event retrievedEvent = cassandraTemplate.selectOne(select, Event.class);

        assertThat(retrievedEvent, IsEqual.equalTo(event));

        List<Event> retrievedEvents = cassandraTemplate.select(select, Event.class);

        assertThat(retrievedEvents.size(), is(1));
        assertThat(retrievedEvents, hasItem(event));
    }
}