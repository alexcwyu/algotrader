package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.api.model.execution.CommissionReport;
import com.unisoft.algotrader.provider.ib.api.model.system.IncomingMessageId;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.*;
/**
 * Created by alex on 8/13/15.
 */
public class CommissionReportEventDeserializer extends Deserializer {


    public CommissionReportEventDeserializer(){
        super(IncomingMessageId.COMMISSION_REPORT);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final CommissionReport commissionReport = new CommissionReport();
        commissionReport.setExecutionId(readString(inputStream));
        commissionReport.setCommission(readDouble(inputStream));
        commissionReport.setCurrencyCode(readString(inputStream));
        commissionReport.setRealizedProfitAndLoss(readDouble(inputStream));
        commissionReport.setYield(readDouble(inputStream));
        commissionReport.setYieldRedemptionDate(readInt(inputStream));
        ibProvider.onCommissionReportEvent(commissionReport);
    }
}