package com.unisoft.algotrader.persistence.cassandra.accessor;

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
    @Query("SELECT * FROM refdata.exchanges WHERE exch_id = :exch_id")
    Exchange get(@Param("exch_id") String exch_id);

    @Query("SELECT * FROM refdata.exchanges")
    Result<Exchange> getAll();

    @Query("SELECT * FROM refdata.exchanges")
    ListenableFuture<Result<Exchange>> getAllAsync();
}