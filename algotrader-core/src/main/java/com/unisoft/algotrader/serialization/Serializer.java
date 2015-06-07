package com.unisoft.algotrader.serialization;

import java.nio.ByteBuffer;

/**
 * Created by alex on 4/20/15.
 */
public interface Serializer{

    public <T> T deserialize(byte[] array) throws Exception;

    public <T> byte[] serialize(T content) throws Exception;


    public <T> T deserialize(ByteBuffer byteBuffer) throws Exception;

    public <T> void serialize(T content, ByteBuffer byteBuffer) throws Exception;

    public void register(Class clazz);

    public int getId(Class clazz);
}
