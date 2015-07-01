package com.unisoft.algotrader.persistence.cassandra.spring;

/**
 * Created by alex on 6/28/15.
 */
import com.datastax.driver.core.utils.UUIDs;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class EventRepositoryIntegrationTest extends BaseIntegrationTest {

    public static final String TIME_BUCKET = "2014-01-01";

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void repositoryStoresAndRetrievesEvents() {
        Event event1 = new Event(UUIDs.timeBased(), "type1", TIME_BUCKET, ImmutableSet.of("tag1", "tag2"));
        Event event2 = new Event(UUIDs.timeBased(), "type1", TIME_BUCKET, ImmutableSet.of("tag3"));
        eventRepository.save(ImmutableSet.of(event1, event2));

        Iterable<Event> events = eventRepository.findByTypeAndBucket("type1", TIME_BUCKET);

        assertThat(events, hasItem(event1));
        assertThat(events, hasItem(event2));
    }

    @Test
    public void repositoryDeletesStoredEvents() {
        Event event1 = new Event(UUIDs.timeBased(), "type1", TIME_BUCKET, ImmutableSet.of("tag1", "tag2"));
        Event event2 = new Event(UUIDs.timeBased(), "type1", TIME_BUCKET, ImmutableSet.of("tag3"));
        eventRepository.save(ImmutableSet.of(event1, event2));

        eventRepository.delete(event1);
        eventRepository.delete(event2);

        Iterable<Event> events = eventRepository.findByTypeAndBucket("type1", TIME_BUCKET);

        assertThat(events, not(hasItem(event1)));
        assertThat(events, not(hasItem(event2)));
    }
}