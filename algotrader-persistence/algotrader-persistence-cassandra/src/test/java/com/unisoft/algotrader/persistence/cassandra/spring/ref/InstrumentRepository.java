package com.unisoft.algotrader.persistence.cassandra.spring.ref;

/**
 * Created by alex on 6/28/15.
 */

import com.unisoft.algotrader.model.refdata.Instrument;
import org.springframework.data.repository.Repository;

public interface InstrumentRepository extends Repository<Instrument, Integer>, InstrumentRepositoryCustom {
    public long count();

    public void delete(Integer s);

    public void delete(Instrument entity);

    public void delete(Iterable<? extends Instrument> entities);

    public void deleteAll();
}