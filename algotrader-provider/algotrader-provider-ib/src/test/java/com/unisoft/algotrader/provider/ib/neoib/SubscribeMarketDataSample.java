package com.unisoft.algotrader.provider.ib.neoib;

import ch.aonyx.broker.ib.api.NeoIbApiClient;
import ch.aonyx.broker.ib.api.contract.Contract;
import ch.aonyx.broker.ib.api.data.MarketDataSubscriptionRequest;
import ch.aonyx.broker.ib.api.net.ConnectionParameters;
import com.unisoft.algotrader.provider.ib.neoib.listener.MyCompositeTickEventListener;
import com.unisoft.algotrader.provider.ib.neoib.listener.MyTickGenericEventListener;
import com.unisoft.algotrader.provider.ib.neoib.listener.MyTickSizeEventListener;

/**
 * Created by alex on 6/24/15.
 */
public class SubscribeMarketDataSample {
    public static void main(String [] args) throws Exception{
        NeoIbApiClient apiClient = new NeoIbApiClient(new MyClientCallback());

        MyConnectionCallback connectionCallback = new MyConnectionCallback();
        apiClient.connect(new ConnectionParameters(), connectionCallback);

        connectionCallback.registerListener(new MyTickSizeEventListener());
        connectionCallback.registerListener(new MyTickGenericEventListener());
        connectionCallback.registerListener(new MyCompositeTickEventListener());

        Contract contract = ContractUtil.getForexContract("EUR");
        connectionCallback.subscribe(new MarketDataSubscriptionRequest(contract));

        Thread.sleep(10000);

        Contract contract2 = ContractUtil.getForexContract("GBP");
        connectionCallback.subscribe(new MarketDataSubscriptionRequest(contract2));
    }
}
