package com.unisoft.algotrader.utils.serialization.msgpack;

import com.google.common.collect.Maps;
import com.unisoft.algotrader.utils.serialization.Serializer;
import org.msgpack.MessagePack;
import org.msgpack.packer.BufferPacker;
import org.msgpack.packer.MessagePackByteBufferPacker;
import org.msgpack.unpacker.BufferUnpacker;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by alex on 4/20/15.
 */
public class MsgpackSerializer implements Serializer {
    private MessagePack msgpack = new MessagePack();

    public final static byte[] MSG_PACK_PROTOCOL = "MsgPack".getBytes();

    AtomicInteger idGenerator = new AtomicInteger(0);

    Map<Class, Integer> class2IdMap = Maps.newHashMap();
    Map<Integer, Class> id2ClassMap = Maps.newHashMap();


    @Override
    public <T> T deserialize(byte[] array) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        return deserialize(byteBuffer) ;
    }

    @Override
    public <T> T deserialize(ByteBuffer byteBuffer) throws Exception{
        BufferUnpacker unpacker = msgpack.createBufferUnpacker(byteBuffer);
        int id = unpacker.readInt();
        Class<T> clazz = id2ClassMap.get(id);
        return unpacker.read(clazz);
    }

    @Override
    public <T> byte[] serialize(T content) throws Exception {
        BufferPacker pk = msgpack.createBufferPacker();
        pk.write(class2IdMap.get(content.getClass()));
        pk.write(content);
        return pk.toByteArray();
    }

    @Override
    public <T> void serialize(T content, ByteBuffer byteBuffer) throws Exception{
        MessagePackByteBufferPacker pk = new MessagePackByteBufferPacker(msgpack, byteBuffer);
        pk.write(class2IdMap.get(content.getClass()));
        pk.write(content);
    }

    public void register(Class clazz){
        if (!class2IdMap.containsKey(clazz)) {
            msgpack.register(clazz);
            Integer id = idGenerator.incrementAndGet();
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