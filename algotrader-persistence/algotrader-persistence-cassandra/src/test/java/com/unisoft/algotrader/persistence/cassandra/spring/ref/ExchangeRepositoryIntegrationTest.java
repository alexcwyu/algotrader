package com.unisoft.algotrader.persistence.cassandra.spring.ref;

/**
 * Created by alex on 6/28/15.
 */

import com.google.common.collect.ImmutableSet;
import com.unisoft.algotrader.core.Exchange;
import com.unisoft.algotrader.persistence.cassandra.spring.BaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class ExchangeRepositoryIntegrationTest extends BaseIntegrationTest {

    public static final String TIME_BUCKET = "2014-01-01";

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Test
    public void repositoryStoresAndRetrievesEvents() {
        Exchange exch1 = new Exchange("SEHK", "HKEX");
        Exchange exch2 = new Exchange("NYSE", "New York Stock Exchange");
        exchangeRepository.save(ImmutableSet.of(exch1, exch2));

        Iterable<Exchange> exchanges = exchangeRepository.findAll();

        assertThat(exchanges, hasItem(exch1));
        assertThat(exchanges, hasItem(exch2));
    }

    @Test
    public void repositoryDeletesStoredEvents() {
        Exchange exch1 = new Exchange("SEHK", "HKEX");
        Exchange exch2 = new Exchange("NYSE", "New York Stock Exchange");
        exchangeRepository.save(ImmutableSet.of(exch1, exch2));

        exchangeRepository.delete(exch1);
        exchangeRepository.delete(exch2);

        Iterable<Exchange> exchanges = exchangeRepository.findAll();

        assertThat(exchanges, not(hasItem(exch1)));
        assertThat(exchanges, not(hasItem(exch2)));
    }
}