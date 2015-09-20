package com.unisoft.algotrader.provider;

/**
 * Created by alex on 5/19/15.
 */
public interface Provider {
    ProviderId providerId();

    boolean connected();

    default void connect(){}

    default void disconnect(){}
}
