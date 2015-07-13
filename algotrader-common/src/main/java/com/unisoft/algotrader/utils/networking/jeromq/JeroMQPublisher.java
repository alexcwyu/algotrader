package com.unisoft.algotrader.utils.networking.jeromq;

import com.unisoft.algotrader.utils.networking.Publisher;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by alex on 4/19/15.
 */
public class JeroMQPublisher implements Publisher {

    private final JeroMQConfig config;

    private ZMQ.Context context;
    private Socket publisher;
    private Socket syncservice;

    private ExecutorService executor;
    private AtomicBoolean connected = new AtomicBoolean(false);

    public JeroMQPublisher(JeroMQConfig config){
        this.config = config;
    }

    @Override
    public void connect() {
        executor = Executors.newFixedThreadPool(1);

        context = ZMQ.context(config.ioThreads);
        publisher = context.socket(ZMQ.PUB);
        publisher.setLinger(config.linger);
        publisher.setSndHWM(config.hwm);
        publisher.bind(config.pubSubAddress);

        syncservice = context.socket(ZMQ.REP);
        syncservice.bind(config.reqRepAddress);
        executor.submit(() -> {
                    int subscribers = 0;
                    while (subscribers < 1) {
                        //  - wait for synchronization request
                        syncservice.recv(0);

                        //  - send synchronization reply
                        syncservice.send("", 0);
                        subscribers++;
                    }
            connected.set(true);
        });
    }

    @Override
    public void disconnect() {
        publisher.close();
        syncservice.close();
        context.term();

        executor.shutdown();
        connected.set(false);
    }

    @Override
    public boolean publish(byte[] bytes) {
        publisher.send(bytes, 0);
        return true;
    }

    @Override
    public boolean publish(ByteBuffer byteBuffer, int length) {
        int limit =byteBuffer.limit();
        byteBuffer.limit(byteBuffer.position() + length);
        publisher.sendByteBuffer(byteBuffer, 0);
        byteBuffer.limit(limit);
        return true;
    }

    @Override
    public boolean connected() {
        return connected.get();
    }
}
