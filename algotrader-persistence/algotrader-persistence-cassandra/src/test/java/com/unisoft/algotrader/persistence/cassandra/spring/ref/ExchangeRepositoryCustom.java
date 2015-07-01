package com.unisoft.algotrader.persistence.cassandra.spring.ref;

import com.unisoft.algotrader.core.Exchange;

/**
 * Created by alex on 6/30/15.
 */
public interface ExchangeRepositoryCustom {

    public Exchange save(Exchange entity);

    public Iterable<Exchange> save(Iterable<Exchange> entities);

    public Exchange save(Exchange entity, long busTime);
}
