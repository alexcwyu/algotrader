package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.execution.ExecutionReportFilter;
import com.unisoft.algotrader.provider.ib.api.model.system.Feature;
import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class ExecutionReportRequestSerializer extends Serializer{

    private static final int VERSION = 3;

    public ExecutionReportRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(long requestId, ExecutionReportFilter filter){
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(OutgoingMessageId.EXECUTION_REPORT_REQUEST.getId());
        builder.append(VERSION);
        if (Feature.EXECUTION_MARKER.isSupportedByVersion(getServerCurrentVersion())) {
            builder.append(requestId);
        }
        appendFilter(builder, filter);
        return builder.toBytes();
    }

    protected void appendFilter(ByteArrayBuilder builder, ExecutionReportFilter filter) {
        builder.append(filter.getClientId());
        builder.append(filter.getAccountNumber());
        builder.append(filter.getTime());
        builder.append(filter.getSymbol());
        builder.append(filter.getSecurityType().getAbbreviation());
        builder.append(filter.getExchange());
        builder.append(filter.getOrderAction().getAbbreviation());
    }

}
