package com.unisoft.algotrader.provider.ib.neoib;

import ch.aonyx.broker.ib.api.NeoIbApiClient;
import ch.aonyx.broker.ib.api.contract.Contract;
import ch.aonyx.broker.ib.api.data.historical.*;
import ch.aonyx.broker.ib.api.net.ConnectionParameters;
import com.unisoft.algotrader.provider.ib.neoib.listener.MyHistoricalDataEventListEventListener;
import com.unisoft.algotrader.provider.ib.neoib.listener.MyHistoricalDataEventListener;

/**
 * Created by alex on 6/24/15.
 */
public class SubscribeHistoricalDataSample {
    public static void main(String [] args) throws Exception{

        NeoIbApiClient apiClient = new NeoIbApiClient(new MyClientCallback());

        MyConnectionCallback connectionCallback = new MyConnectionCallback();
        apiClient.connect(new ConnectionParameters(), connectionCallback);

        connectionCallback.registerListener(new MyHistoricalDataEventListener());
        connectionCallback.registerListener(new MyHistoricalDataEventListEventListener());

        Contract contract = ContractUtil.getStockContract("2800", "SEHK", "HKD");

        String date = "20150601 00:00:00";

        connectionCallback.subscribe(new HistoricalDataSubscriptionRequest(contract,
                date,
                new TimeSpan(1, TimeSpanUnit.MONTH),
                BarSize.ONE_HOUR,
                HistoricalDataType.TRADES, false, DateFormat.YYYYMMDD__HH_MM_SS));

    }
}
