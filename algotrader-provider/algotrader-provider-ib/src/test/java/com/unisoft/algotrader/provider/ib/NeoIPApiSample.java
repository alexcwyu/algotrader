package com.unisoft.algotrader.provider.ib;

import ch.aonyx.broker.ib.api.*;
import ch.aonyx.broker.ib.api.contract.*;
import ch.aonyx.broker.ib.api.data.*;
import ch.aonyx.broker.ib.api.net.ConnectionCallback;
import ch.aonyx.broker.ib.api.net.ConnectionException;
import ch.aonyx.broker.ib.api.net.ConnectionParameters;

/**
 * Created by alex on 6/20/15.
 */
public class NeoIPApiSample {
    public static void main(final String[] args) {
        new NeoIPApiSample();
    }

    public static class MyClientCallback implements ClientCallback{
        @Override
        public void onSuccess(CallbackObject object) {

        }

        @Override
        public void onFailure(NeoIbApiClientException exception) {

        }
    }
    public class MyContractSpecificationEventListener implements ContractSpecificationEventListener {
        @Override
        public void notify(ContractSpecificationEvent event) {

        }
    }

    public class MyTickSizeEventListener implements TickSizeEventListener {
        @Override
        public void notify(TickSizeEvent event) {

        }
    }

    public class MyTickGenericEventListener implements TickGenericEventListener {
        @Override
        public void notify(TickGenericEvent event) {

        }
    }

    public class MyCompositeTickEventListener implements CompositeTickEventListener {
        @Override
        public void notify(CompositeTickEvent event) {

        }
    }

    private NeoIPApiSample() {
        final NeoIbApiClient apiClient = new NeoIbApiClient(new MyClientCallback());
        apiClient.connect(new ConnectionParameters(1), new ConnectionCallback() {

            @Override
            public void onSuccess(final Session session) {
                session.registerListener(new MyContractSpecificationEventListener());
                session.registerListener(new MyTickSizeEventListener());
                session.registerListener(new MyTickGenericEventListener());
                session.registerListener(new MyCompositeTickEventListener());

                Contract contract = getContract("DAX");
                session.request(new ContractSpecificationRequest(contract));
                session.subscribe(new MarketDataSubscriptionRequest(contract));

                session.start();
            }

            private Contract getContract(final String symbol) {
                final Contract contract = new Contract();
                contract.setCurrencyCode("EUR");
                contract.setExchange("DTB");
                contract.setExpiry("201306");
                contract.setSecurityType(SecurityType.FUTURE);
                contract.setSymbol(symbol);
                return contract;
            }

            @Override
            public void onFailure(final ConnectionException exception) {
            }
        });
    }
}
