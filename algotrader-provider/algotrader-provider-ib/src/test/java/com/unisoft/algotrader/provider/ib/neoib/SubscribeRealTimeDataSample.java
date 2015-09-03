package com.unisoft.algotrader.provider.ib.neoib;

import ch.aonyx.broker.ib.api.NeoIbApiClient;
import ch.aonyx.broker.ib.api.contract.Contract;
import ch.aonyx.broker.ib.api.data.bar.RealTimeBarDataType;
import ch.aonyx.broker.ib.api.data.bar.RealTimeBarSubscriptionRequest;
import ch.aonyx.broker.ib.api.net.ConnectionParameters;
import com.unisoft.algotrader.provider.ib.neoib.listener.MyRealTimeBarEventListener;

/**
 * Created by alex on 6/24/15.
 */
public class SubscribeRealTimeDataSample {
    public static void main(String [] args) throws Exception{
        NeoIbApiClient apiClient = new NeoIbApiClient(new MyClientCallback());

        MyConnectionCallback connectionCallback = new MyConnectionCallback();
        apiClient.connect(new ConnectionParameters("localhost", 4001, 2), connectionCallback);

        connectionCallback.registerListener(new MyRealTimeBarEventListener());

        Contract contract = ContractUtil.getForexContract("EUR");
        connectionCallback.subscribe(new RealTimeBarSubscriptionRequest(contract, 5, RealTimeBarDataType.MID_POINT, false));

        Thread.sleep(10000);

        //Contract contract2 = ContractUtil.getForexContract("GBP");
        //connectionCallback.subscribe(new MarketDataSubscriptionRequest(contract2));
    }
}
