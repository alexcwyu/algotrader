package com.unisoft.algotrader.utils.networking.jeromq;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.utils.networking.DataHandler;
import com.unisoft.algotrader.utils.networking.Subscriber;
import org.zeromq.ZMQ;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by alex on 4/19/15.
 */
public class JeroMQSubscriber implements Subscriber {


    private final JeroMQConfig config;
    private final List<DataHandler> dataHandlers = Lists.newArrayList();

    private ZMQ.Context context;
    private ZMQ.Socket subscriber;
    private ZMQ.Socket syncclient;

    private ExecutorService executor;
    private AtomicBoolean connected = new AtomicBoolean(false);
    private Future subscriptionLoop;

    public JeroMQSubscriber(JeroMQConfig config){
        this.config = config;
    }

    @Override
    public void connect() {
        executor = Executors.newFixedThreadPool(1);

        context = ZMQ.context(config.ioThreads);

        subscriber = context.socket(ZMQ.SUB);
        subscriber.connect(config.pubSubAddress);
        subscriber.subscribe(config.topic);

        syncclient = context.socket(ZMQ.REQ);
        syncclient.connect(config.reqRepAddress);
        executor.submit(() -> {
            syncclient.send(ZMQ.MESSAGE_SEPARATOR, 0);
            syncclient.recv(0);
            subscriptionLoop = executor.submit(()->{
                while (true) {
                    byte[] data = subscriber.recv();
                    for (DataHandler dataHandler : dataHandlers){
                        dataHandler.onData(data);
                    }

                }
            });
            connected.set(true);
        });


    }

    @Override
    public void disconnect() {
        subscriber.close();
        syncclient.close();
        context.term();

        subscriptionLoop.cancel(true);
        executor.shutdown();
        connected.set(false);
    }

    @Override
    public boolean connected() {
        return connected.get();
    }

    @Override
    public void subscribe(DataHandler handler) {
        dataHandlers.add(handler);
    }
}
