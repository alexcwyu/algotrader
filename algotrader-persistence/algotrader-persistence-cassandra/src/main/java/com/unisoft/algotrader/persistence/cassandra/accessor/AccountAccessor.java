package com.unisoft.algotrader.persistence.cassandra.accessor;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.util.concurrent.ListenableFuture;
import com.unisoft.algotrader.model.trading.Account;

/**
 * Created by alex on 7/8/15.
 */
@Accessor
public interface AccountAccessor {

    @Query("SELECT * FROM trading.accounts WHERE account_id = :account_id")
    Account get(@Param("account_id") String account_id);

    @Query("SELECT * FROM trading.accounts")
    Result<Account> getAll();

    @Query("SELECT * FROM trading.accounts")
    ListenableFuture<Result<Account>> getAllAsync();
}
