package com.unisoft.algotrader.provider.ib.neoib;

import ch.aonyx.broker.ib.api.NeoIbApiClient;
import ch.aonyx.broker.ib.api.Session;
import ch.aonyx.broker.ib.api.contract.Contract;
import ch.aonyx.broker.ib.api.contract.ContractSpecificationRequest;
import ch.aonyx.broker.ib.api.data.MarketDataSubscriptionRequest;
import ch.aonyx.broker.ib.api.net.ConnectionCallback;
import ch.aonyx.broker.ib.api.net.ConnectionException;
import ch.aonyx.broker.ib.api.net.ConnectionParameters;
import com.unisoft.algotrader.provider.ib.neoib.listener.MyCompositeTickEventListener;
import com.unisoft.algotrader.provider.ib.neoib.listener.MyContractSpecificationEventListener;
import com.unisoft.algotrader.provider.ib.neoib.listener.MyTickGenericEventListener;
import com.unisoft.algotrader.provider.ib.neoib.listener.MyTickSizeEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/20/15.
 */
public class ApiSample {
    public static void main(final String[] args) {
        new ApiSample();
    }

    private static final Logger LOG = LogManager.getLogger(ApiSample.class);

    private ApiSample() {
        final NeoIbApiClient apiClient = new NeoIbApiClient(new MyClientCallback());
        apiClient.connect(new ConnectionParameters(1), new ConnectionCallback() {

            @Override
            public void onSuccess(final Session session) {
                session.registerListener(new MyContractSpecificationEventListener());
                session.registerListener(new MyTickSizeEventListener());
                session.registerListener(new MyTickGenericEventListener());
                session.registerListener(new MyCompositeTickEventListener());

                Contract contract = ContractUtil.getForexContract("EUR");
                session.request(new ContractSpecificationRequest(contract));
                session.subscribe(new MarketDataSubscriptionRequest(contract));

                session.start();
            }

            @Override
            public void onFailure(final ConnectionException exception) {
            }
        });
    }
}
