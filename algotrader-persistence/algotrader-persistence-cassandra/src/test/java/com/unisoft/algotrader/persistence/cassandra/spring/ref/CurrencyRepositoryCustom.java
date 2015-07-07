package com.unisoft.algotrader.persistence.cassandra.spring.ref;

import com.unisoft.algotrader.model.refdata.Currency;

/**
 * Created by alex on 6/30/15.
 */
public interface CurrencyRepositoryCustom {

    public Currency save(Currency entity);

    public Iterable<Currency> save(Iterable<Currency> entities);

    public Currency save(Currency entity, long busTime);
}
