package com.unisoft.algotrader.networking;

import com.unisoft.algotrader.event.data.Bar;
import com.unisoft.algotrader.event.data.Quote;
import com.unisoft.algotrader.event.data.Trade;
import com.unisoft.algotrader.networking.aeron.AeronUtils;
import com.unisoft.algotrader.serialization.MessageHeader;
import com.unisoft.algotrader.serialization.Serializer;
import com.unisoft.algotrader.serialization.kryo.KryoSerializer;
import com.unisoft.algotrader.serialization.msgpack.MsgpackSerializer;
import org.testng.annotations.Test;
import uk.co.real_logic.aeron.common.RateReporter;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by alex on 5/16/15.
 */
public abstract class AbstractPubSubTest {

    public static int ITER = 100_000_000;

    public static void printSubRate(
            final double messagesPerSec,
            final double bytesPerSec,
            final long totalMessages,
            final long totalBytes)
    {
        System.out.println(
                String.format(
                        "SUB: %.02g msgs/sec, %.02g bytes/sec, totals %d messages %d MB",
                        messagesPerSec, bytesPerSec, totalMessages, totalBytes / (1024 * 1024)));
    }

    public static void printPubRate(
            final double messagesPerSec,
            final double bytesPerSec,
            final long totalMessages,
            final long totalBytes)
    {
        System.out.println(
                String.format(
                        "PUB %.02g msgs/sec, %.02g bytes/sec, totals %d messages %d MB",
                        messagesPerSec, bytesPerSec, totalMessages, totalBytes / (1024 * 1024)));
    }


    public static class TradeHandler implements DataHandler {
        private final int expected;
        private final CountDownLatch latch;
        private final Serializer serializer;

        private int count;
        public TradeHandler(int expected, CountDownLatch latch, Serializer serializer){
            this.expected = expected;
            this.latch = latch;
            this.serializer = serializer;
        }

        @Override
        public void onData(byte[] bytes) {
           // System.out.println(Arrays.toString(bytes));

            ByteBuffer wrapped = ByteBuffer.wrap(bytes);

            try {
                //MessageHeader header = serializer.deserialize(wrapped);
                Trade trade = serializer.deserialize(wrapped);

                //assert header.msgId == count;
                //assert trade.instId == count;
                count++;
                if (count == expected)
                    latch.countDown();
            }
            catch (Exception e){
                System.out.println("count =" + count + ", bytes=" + bytes.length+"\n");
                        e.printStackTrace();
                System.exit(-1);
            }

        }
    }


    public static class NumberCountHandler implements DataHandler{
        private final int expected;
        private final CountDownLatch latch;

        private int count;
        public NumberCountHandler(int expected, CountDownLatch latch){
            this.expected = expected;
            this.latch = latch;

        }

        @Override
        public void onData(byte[] bytes) {
            count++;
            if (count == expected)
                latch.countDown();
        }
    }

    public static class NumberCountProducer implements Runnable{

        private final int expected;
        private final CountDownLatch startLatch;
        private final CountDownLatch latch;
        private int count;
        private RateReporter pubReporter;
        private Publisher publisher;

        public NumberCountProducer(int expected, CountDownLatch startLatch, CountDownLatch latch, RateReporter pubReporter, Publisher publisher){
            this.expected = expected;
            this.startLatch = startLatch;
            this.latch = latch;
            this.pubReporter = pubReporter;
            this.publisher = publisher;
        }

        public void run(){

            ByteBuffer buffer = ByteBuffer.allocate(4);

            try {
                startLatch.await();
            }
            catch(Exception e){

            }
            while (count<expected)
            {
                byte [] bytes = buffer.putInt(count).array();
                int length = bytes.length;
                publisher.publish(bytes);
                if (pubReporter!=null) {
                    pubReporter.onMessage(1, length);
                }
                buffer.clear();
                count++;
            }
            latch.countDown();
        }
    }

    public static class TradeProducer implements Runnable{

        private final int expected;
        private final CountDownLatch startLatch;
        private final CountDownLatch latch;
        private final RateReporter pubReporter;
        private final Publisher publisher;
        private final Serializer serializer;
        private int count;

        public TradeProducer(int expected, CountDownLatch startLatch, CountDownLatch latch, RateReporter pubReporter, Publisher publisher, Serializer serializer){
            this.expected = expected;
            this.startLatch = startLatch;
            this.latch = latch;
            this.pubReporter = pubReporter;
            this.publisher = publisher;
            this.serializer = serializer;
        }

        public void run(){

            ByteBuffer buffer = ByteBuffer.allocate(4096);

            try {
                startLatch.await();
            }
            catch(Exception e){

            }

            Trade trade = new Trade("HSI", System.currentTimeMillis(), 99, 85);
            while (count<expected)
            {


                try {
//                    MessageHeader header = new MessageHeader();
//                    header.msgId = count;
//                    header.typeId = serializer.getId(trade.getClass());

                    trade.instId = "HSI";

//                    serializer.serialize(header, buffer);
                    serializer.serialize(trade, buffer);
                    int length = buffer.position();
                    buffer.flip();
                    //byte []bytes = new byte[buffer.remaining()];
                    //buffer.get(bytes, 0, bytes.count);

                    //int count = bytes.count;
                    publisher.publish(buffer, length);


                    if (pubReporter!=null) {
                        pubReporter.onMessage(1, length);
                    }
                    buffer.clear();
                    count++;
                }
                catch(Exception e){
                    e.printStackTrace();
                    System.exit(-1);

                }
            }
            latch.countDown();
        }
    }

    public abstract Subscriber createSubScriber();
    public abstract Publisher createPublisher();


    public void test(CountDownLatch startLatch, CountDownLatch latch,
                     Subscriber subscriber, Publisher publisher,
                     RateReporter subReporter, RateReporter pubReporter,
                     DataHandler handler, Runnable producer) throws Exception{

        ExecutorService executor = Executors.newFixedThreadPool(5);

        subscriber.subscribe(AeronUtils.rateReporterHandler(subReporter));
        subscriber.subscribe(handler);

        subscriber.connect();
        publisher.connect();

        while(!publisher.connected()||!subscriber.connected()){
            Thread.yield();
        }

        executor.execute(subReporter);
        executor.execute(pubReporter);
        executor.execute(producer);

        System.out.println("Starting");
        long start = System.currentTimeMillis();
        startLatch.countDown();

        latch.await();
        long end = System.currentTimeMillis();

        long opsPerSecond = (ITER * 1000L) / (end - start);

        System.out.format("OPS=%,d ops/sec\n", Long.valueOf(opsPerSecond));
        executor.shutdown();
    }

    @Test
    public void test_number() throws Exception{
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch latch = new CountDownLatch(2);

        Subscriber subscriber = createSubScriber();
        Publisher publisher = createPublisher();

        RateReporter subReporter = new RateReporter(TimeUnit.SECONDS.toNanos(1), AbstractPubSubTest::printSubRate);
        RateReporter pubReporter = new RateReporter(TimeUnit.SECONDS.toNanos(1), AbstractPubSubTest::printPubRate);

        DataHandler handler = new NumberCountHandler(ITER, latch);
        Runnable producer = new NumberCountProducer(ITER, startLatch, latch, pubReporter, publisher);

        test(startLatch, latch, subscriber, publisher, subReporter, pubReporter, handler, producer);
    }

    @Test
    public void test_kyro()throws Exception{
        Serializer serializer = new KryoSerializer();

        serializer.register(Quote.class);
        serializer.register(Trade.class);
        serializer.register(Bar.class);
        serializer.register(MessageHeader.class);


        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch latch = new CountDownLatch(2);

        Subscriber subscriber = createSubScriber();
        Publisher publisher = createPublisher();

        RateReporter subReporter = new RateReporter(TimeUnit.SECONDS.toNanos(1), AbstractPubSubTest::printSubRate);
        RateReporter pubReporter = new RateReporter(TimeUnit.SECONDS.toNanos(1), AbstractPubSubTest::printPubRate);

        DataHandler handler = new TradeHandler(ITER, latch, serializer);
        Runnable producer = new TradeProducer(ITER, startLatch, latch, pubReporter, publisher, serializer);

        test(startLatch, latch, subscriber, publisher, subReporter, pubReporter, handler, producer);
    }

    @Test
    public void test_msgpack()throws Exception{
        Serializer serializer = new MsgpackSerializer();

        serializer.register(Quote.class);
        serializer.register(Trade.class);
        serializer.register(Bar.class);
        serializer.register(MessageHeader.class);


        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch latch = new CountDownLatch(2);

        Subscriber subscriber = createSubScriber();
        Publisher publisher = createPublisher();

        RateReporter subReporter = new RateReporter(TimeUnit.SECONDS.toNanos(1), AbstractPubSubTest::printSubRate);
        RateReporter pubReporter = new RateReporter(TimeUnit.SECONDS.toNanos(1), AbstractPubSubTest::printPubRate);

        DataHandler handler = new TradeHandler(ITER, latch, serializer);
        Runnable producer = new TradeProducer(ITER, startLatch, latch, pubReporter, publisher, serializer);

        test(startLatch, latch, subscriber, publisher, subReporter, pubReporter, handler, producer);
    }
}
