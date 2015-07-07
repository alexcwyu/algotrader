package com.unisoft.algotrader.persistence.cassandra.accessor;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.util.concurrent.ListenableFuture;
import com.unisoft.algotrader.model.trading.Portfolio;

/**
 * Created by alex on 7/8/15.
 */
@Accessor
public interface PortfolioAccessor {

    @Query("SELECT * FROM trading.portfolios WHERE portfolio_id = :portfolio_id")
    Portfolio get(@Param("portfolio_id") String portfolio_id);

    @Query("SELECT * FROM trading.portfolios")
    Result<Portfolio> getAll();

    @Query("SELECT * FROM trading.portfolios")
    ListenableFuture<Result<Portfolio>> getAllAsync();
}
