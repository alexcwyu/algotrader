package com.unisoft.algotrader.networking;

/**
 * Created by alex on 4/19/15.
 */
public interface Subscriber {

    public void connect();

    public void disconnect();

    public boolean connected();

    public void subscribe(DataHandler handler);
}
