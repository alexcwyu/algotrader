package com.unisoft.algotrader.persistence.cassandra.spring.ref;

import com.unisoft.algotrader.model.refdata.Instrument;

/**
 * Created by alex on 6/30/15.
 */
public interface InstrumentRepositoryCustom {

    public Instrument save(Instrument entity);

    public Iterable<Instrument> save(Iterable<Instrument> instruments);

    public Instrument save(Instrument instrument, long busTime);

    public Instrument findOne(Integer s) ;

    public Iterable<Instrument> findAll();

    public Iterable<Instrument> findAll(Iterable<Integer> ids);
}
