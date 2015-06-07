package com.unisoft.algotrader.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.*;
import com.google.common.collect.Maps;
import com.unisoft.algotrader.serialization.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by alex on 4/20/15.
 */
public class KryoSerializer implements Serializer{

    Kryo kryo = new Kryo();

    AtomicInteger idGenerator = new AtomicInteger(0);

    Map<Class, Integer> class2IdMap = Maps.newHashMap();
    Map<Integer, Class> id2ClassMap = Maps.newHashMap();


    @Override
    public <T> T deserialize(byte[] array) throws Exception {
        Input input = new Input(new ByteArrayInputStream(array));
        return deserialize(input);
    }


    @Override
    public <T> T deserialize(ByteBuffer byteBuffer) throws Exception{
        Input input = new ByteBufferInput(byteBuffer);
        return deserialize(input);
    }

    private <T> T deserialize(Input input){
        int id = input.readInt();
        Class<T> clazz = id2ClassMap.get(id);
        return kryo.readObject(input, clazz);
    }

    @Override
    public <T> byte[] serialize(T content) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Output output = new Output(stream);
        output.writeInt(class2IdMap.get(content.getClass()));
        kryo.writeObject(output, content);
        output.close();
        return stream.toByteArray();
    }

    @Override
    public <T> void serialize(T content, ByteBuffer byteBuffer) throws Exception{
        Output output = new ByteBufferOutput(byteBuffer);
        output.writeInt(class2IdMap.get(content.getClass()));
        kryo.writeObject(output, content);
        output.close();
    }

    public void register(Class clazz){
        if (!class2IdMap.containsKey(clazz)) {
            Integer id = idGenerator.getAndIncrement();
            kryo.register(clazz, id);
            class2IdMap.put(clazz, id);
            id2ClassMap.put(id, clazz);
        }
    }

    public int getId(Class clazz){
        if (class2IdMap.containsKey(clazz)) {
            return class2IdMap.get(clazz);
        }
        return -1;
    }
}
