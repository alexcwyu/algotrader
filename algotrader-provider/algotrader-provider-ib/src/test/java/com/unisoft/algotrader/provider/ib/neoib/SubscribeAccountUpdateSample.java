package com.unisoft.algotrader.provider.ib.neoib;

import ch.aonyx.broker.ib.api.NeoIbApiClient;
import ch.aonyx.broker.ib.api.account.AccountUpdateSubscriptionRequest;
import ch.aonyx.broker.ib.api.net.ConnectionParameters;
import com.unisoft.algotrader.provider.ib.neoib.listener.MyAccountUpdateTimeEventListener;
import com.unisoft.algotrader.provider.ib.neoib.listener.MyAccountUpdateValueEndEventListener;
import com.unisoft.algotrader.provider.ib.neoib.listener.MyAccountUpdateValueEventListener;

/**
 * Created by alex on 6/24/15.
 */
public class SubscribeAccountUpdateSample {
    public static void main(String [] args) throws Exception{

        NeoIbApiClient apiClient = new NeoIbApiClient(new MyClientCallback());

        MyConnectionCallback connectionCallback = new MyConnectionCallback();
        apiClient.connect(new ConnectionParameters(), connectionCallback);

        connectionCallback.registerListener(new MyAccountUpdateTimeEventListener());
        connectionCallback.registerListener(new MyAccountUpdateValueEndEventListener());
        connectionCallback.registerListener(new MyAccountUpdateValueEventListener());

        connectionCallback.subscribe(new AccountUpdateSubscriptionRequest("DU212517"));

    }
}
