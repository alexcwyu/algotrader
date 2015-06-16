package com.unisoft.algotrader.networking.old;

import com.unisoft.algotrader.networking.aeron.AeronUtils;
import com.unisoft.algotrader.networking.jeromq.JeroMQConfig;
import com.unisoft.algotrader.networking.jeromq.JeroMQSubscriber;
import uk.co.real_logic.aeron.common.RateReporter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alex on 4/19/15.
 */
public class JeroMQSubscriberTest {

    public static void main(String [] args)throws Exception{
        JeroMQSubscriber subscriber = new JeroMQSubscriber(new JeroMQConfig.JeroMQConfigBuilder().build());
        ExecutorService executor = Executors.newFixedThreadPool(1);

        RateReporter reporter = AeronUtils.simpleRateReporter();
        subscriber.subscribe(AeronUtils.rateReporterHandler(reporter));
        executor.execute(reporter);

        subscriber.connect();

        System.out.println("start receiving.");
        Thread.currentThread().join();
        System.out.println("done receiving.");
    }
}
