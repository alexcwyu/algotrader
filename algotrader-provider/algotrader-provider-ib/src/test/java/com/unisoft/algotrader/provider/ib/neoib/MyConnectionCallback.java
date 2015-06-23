package com.unisoft.algotrader.provider.ib.neoib;

import ch.aonyx.broker.ib.api.*;
import ch.aonyx.broker.ib.api.net.ConnectionCallback;
import ch.aonyx.broker.ib.api.net.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by alex on 6/24/15.
 */
public class MyConnectionCallback implements ConnectionCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyConnectionCallback.class);

    private Session session;
    @Override
    public void onSuccess(final Session session) {
        this.session = session;
        connect();
    }

    @Override
    public void onFailure(final ConnectionException exception) {
        LOGGER.error("failure due to exception: ", exception);
    }

    public void request(SimpleRequest request){
        session.request(request);
    }

    public void orderRequest(OrderRequest request){
        session.orderRequest(request);
    }

    public void subscribe(SubscriptionRequest request){
        session.subscribe(request);
    }

    public void unsubscribe(UnsubscriptionRequest request){
        session.unsubscribe(request);
    }

    public <E extends Event> void registerListener(EventListener<E> listener){
        session.registerListener(listener);
    }

    public <E extends Event> void unregisterListener(EventListener<E> listener){
        session.unregisterListener(listener);
    }

    public void connect(){
        session.start();
    }

    public boolean isConnected(){
        return session != null && session.isStarted();
    }

    public void disconnect(){
        session.stop();
    }

}
