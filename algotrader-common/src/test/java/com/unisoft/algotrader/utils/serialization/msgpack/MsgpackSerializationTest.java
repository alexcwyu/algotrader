package com.unisoft.algotrader.utils.serialization.msgpack;

import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.model.event.data.Quote;
import com.unisoft.algotrader.model.event.data.Trade;
import com.unisoft.algotrader.utils.serialization.MessageHeader;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 5/16/15.
 */
public class MsgpackSerializationTest {
    public static MsgpackSerializer serializer;


    @BeforeClass
    public static void setup(){
        serializer = new MsgpackSerializer();

        //serializer.register(Instrument.AltId.class);
        serializer.register(Quote.class);
        serializer.register(Trade.class);
        serializer.register(Bar.class);
        serializer.register(MessageHeader.class);
    }

    @Test
    public void testSingleMessage()throws Exception{
        Trade trade = new Trade(127, System.currentTimeMillis(), 121,85);
        byte [] traderByte = serializer.serialize(trade);
        Trade trade2 = serializer.deserialize(traderByte);
        Assert.assertEquals(trade, trade2);

        Quote quote = new Quote(127, System.currentTimeMillis(), 121,122, 50,90);
        byte [] quoteByte = serializer.serialize(quote);
        Quote quote2 = serializer.deserialize(quoteByte);
        Assert.assertEquals(quote, quote2);
    }

    @Test
    public void testMultipleMessages() throws Exception{
        final ByteBuffer buffer = ByteBuffer.allocateDirect(4096);

        int test = 1024;
        Object obj = null;
        for (int i =0; i< test ; i ++) {
            if (obj == null || obj.getClass() == Trade.class) {
                obj = new Quote(127, System.currentTimeMillis(), i, 122, 50, 90);
            }
            else {
                obj = new Trade(127, System.currentTimeMillis(), i, 85);
            }

            MessageHeader header = new MessageHeader();
            header.msgId = i;
            header.typeId = serializer.getId(obj.getClass());
            serializer.serialize(header, buffer);

            serializer.serialize(obj, buffer);
            buffer.flip();

            MessageHeader header2 = serializer.deserialize(buffer);
            Object obj2 = serializer.deserialize(buffer);

            buffer.clear();

            assertEquals(header, header2);
            assertEquals(obj, obj2);
        }
    }

    @Test
    public void testSequenceMessages() throws Exception{
        final ByteBuffer buffer = ByteBuffer.allocateDirect(4096);

        int test = 10;
        for (int i =0; i< test ; i ++) {
            Trade obj = new Trade(127, System.currentTimeMillis(), i, 85);

            MessageHeader header = new MessageHeader();
            header.msgId = i;
            header.typeId = serializer.getId(obj.getClass());
            serializer.serialize(header, buffer);
            serializer.serialize(obj, buffer);
        }

        buffer.flip();

        for (int i =0; i< test ; i ++) {
            MessageHeader header2 = serializer.deserialize(buffer);
            Trade obj2 = serializer.deserialize(buffer);


            assertEquals(i, header2.msgId);
            Assert.assertEquals(i, obj2.price, 0.000001);
            Assert.assertEquals(85, obj2.size);
        }

        buffer.clear();
    }
}
