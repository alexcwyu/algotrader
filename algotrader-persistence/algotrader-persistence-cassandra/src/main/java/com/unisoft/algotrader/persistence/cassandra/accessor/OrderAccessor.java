package com.unisoft.algotrader.persistence.cassandra.accessor;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.util.concurrent.ListenableFuture;
import com.unisoft.algotrader.model.event.execution.Order;

/**
 * Created by alex on 7/8/15.
 */
@Accessor
public interface OrderAccessor {

    @Query("SELECT * FROM trading.orders WHERE order_id = :order_id")
    Order get(@Param("order_id") String order_id);

    @Query("SELECT * FROM trading.orders")
    Result<Order> getAll();

    @Query("SELECT * FROM trading.orders")
    ListenableFuture<Result<Order>> getAllAsync();

    @Query("SELECT * FROM trading.orders WHERE inst_id = :inst_id")
    Result<Order> getByInstId(@Param("inst_id") long inst_id);

    @Query("SELECT * FROM trading.orders WHERE portfolio_id = :portfolio_id")
    Result<Order> getByPortfolioId(@Param("portfolio_id") String portfolio_id);

    @Query("SELECT * FROM trading.orders WHERE strategy_id = :strategy_id")
    Result<Order> getByStrategyId(@Param("strategy_id") String strategy_id);
}
