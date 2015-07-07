package com.unisoft.algotrader.persistence.cassandra;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.util.concurrent.ListenableFuture;
import com.unisoft.algotrader.model.refdata.Exchange;

/**
 * Created by alex on 7/7/15.
 */

@Accessor
public interface ExchangeAccessor {
    @Query("SELECT * FROM refdata.exchange WHERE exch_id = :exch_id LIMIT 1")
    Exchange getOne(@Param("exch_id") String exch_id);

    @Query("SELECT * FROM refdata.exchange WHERE exch_id = :exch_id")
    Result<Exchange> get(@Param("exch_id") String exch_id);

    @Query("SELECT * FROM refdata.exchange")
    Result<Exchange> getAll();

    @Query("SELECT * FROM refdata.exchange")
    ListenableFuture<Result<Exchange>> getAllAsync();
}