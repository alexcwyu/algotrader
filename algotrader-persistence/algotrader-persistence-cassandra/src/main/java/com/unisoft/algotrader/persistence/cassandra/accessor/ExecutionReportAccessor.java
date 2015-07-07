package com.unisoft.algotrader.persistence.cassandra.accessor;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.util.concurrent.ListenableFuture;
import com.unisoft.algotrader.model.event.execution.ExecutionReport;

/**
 * Created by alex on 7/8/15.
 */
@Accessor
public interface ExecutionReportAccessor {

    @Query("SELECT * FROM trading.execution_reports WHERE exec_id = :exec_id")
    ExecutionReport get(@Param("exec_id") String exec_id);

    @Query("SELECT * FROM trading.execution_reports")
    Result<ExecutionReport> getAll();

    @Query("SELECT * FROM trading.execution_reports")
    ListenableFuture<Result<ExecutionReport>> getAllAsync();

    @Query("SELECT * FROM trading.execution_reports WHERE inst_id = :inst_id")
    Result<ExecutionReport> getByInstId(@Param("inst_id") int inst_id);

    @Query("SELECT * FROM trading.execution_reports WHERE order_id = :order_id")
    Result<ExecutionReport> getByOrderId(@Param("order_id") long order_id);
}
