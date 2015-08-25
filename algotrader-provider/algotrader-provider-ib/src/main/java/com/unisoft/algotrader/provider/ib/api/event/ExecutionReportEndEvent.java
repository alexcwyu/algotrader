package com.unisoft.algotrader.provider.ib.api.event;

/**
 * Created by alex on 8/26/15.
 */
public class ExecutionReportEndEvent extends IBEvent<ExecutionReportEndEvent>  {


    public ExecutionReportEndEvent(final String requestId){
        super(requestId);
    }

}