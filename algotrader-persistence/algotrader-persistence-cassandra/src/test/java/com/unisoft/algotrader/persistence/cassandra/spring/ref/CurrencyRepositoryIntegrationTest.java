package com.unisoft.algotrader.persistence.cassandra.spring.ref;

/**
 * Created by alex on 6/28/15.
 */

import com.google.common.collect.ImmutableSet;
import com.unisoft.algotrader.model.refdata.Currency;
import com.unisoft.algotrader.persistence.cassandra.spring.BaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class CurrencyRepositoryIntegrationTest extends BaseIntegrationTest {

    public static final String TIME_BUCKET = "2014-01-01";

    @Autowired
    private CurrencyRepository ccyRepo;

    @Test
    public void repositoryStoresAndRetrievesEvents() {
        Currency ccy1 = new Currency("HKD", "HK Dollar");
        Currency ccy2 = new Currency("USD", "US Dollar");
        ccyRepo.save(ImmutableSet.of(ccy1, ccy2));

        Iterable<Currency> events = ccyRepo.findAll();

        assertThat(events, hasItem(ccy1));
        assertThat(events, hasItem(ccy1));
    }

    @Test
    public void repositoryDeletesStoredEvents() {
        Currency ccy1 = new Currency("HKD", "HK Dollar");
        Currency ccy2 = new Currency("USD", "US Dollar");
        ccyRepo.save(ImmutableSet.of(ccy1, ccy2));

        ccyRepo.delete(ccy1);
        ccyRepo.delete(ccy2);

        Iterable<Currency> events = ccyRepo.findAll();

        assertThat(events, not(hasItem(ccy1)));
        assertThat(events, not(hasItem(ccy2)));
    }
}