package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.provider.ib.api.IBConstants;
import com.unisoft.algotrader.provider.ib.api.IBSession;
import com.unisoft.algotrader.provider.ib.api.IncomingMessageId;
import com.unisoft.algotrader.provider.ib.api.model.CommissionReport;

import java.io.InputStream;
import static com.unisoft.algotrader.provider.ib.api.InputStreamUtils.*;
/**
 * Created by alex on 8/13/15.
 */
public class CommissionReportEventDeserializer extends Deserializer {


    public CommissionReportEventDeserializer(int serverCurrentVersion){
        super(IncomingMessageId.COMMISSION_REPORT, serverCurrentVersion);
    }

    @Override
    public void consumeVersionLess(InputStream inputStream, IBSession ibSession) {
        final CommissionReport commissionReport = new CommissionReport();
        commissionReport.setExecutionId(readString(inputStream));
        commissionReport.setCommission(readDouble(inputStream));
        commissionReport.setCurrencyCode(readString(inputStream));
        commissionReport.setRealizedProfitAndLoss(readDouble(inputStream));
        commissionReport.setYield(readDouble(inputStream));
        commissionReport.setYieldRedemptionDate(readInt(inputStream));
        ibSession.onCommissionReport(commissionReport);
    }
}