package com.unisoft.algotrader.networking;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * Created by alex on 4/19/15.
 */
public interface Publisher{

    public void connect();

    public void disconnect();

    public boolean publish(byte[] bytes);

    public boolean publish(ByteBuffer byteBuffer, int length);

    public boolean connected();
}
