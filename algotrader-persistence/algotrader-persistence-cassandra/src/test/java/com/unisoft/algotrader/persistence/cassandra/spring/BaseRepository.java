package com.unisoft.algotrader.persistence.cassandra.spring;

import com.unisoft.algotrader.core.Currency;

import java.io.Serializable;

/**
 * Created by alex on 6/30/15.
 */
public interface BaseRepository<T, ID extends Serializable> {

    public T findOne(ID s) ;

    public boolean exists(ID s);

    public Iterable<T> findAll();

    public Iterable<T> findAll(Iterable<ID> strings);

    public long count();

    public void delete(ID s);

    public void delete(T entity);

    public void delete(Iterable<? extends T> entities);

    public void deleteAll();
}
