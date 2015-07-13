package com.unisoft.algotrader.utils.networking.old;

import com.unisoft.algotrader.utils.networking.aeron.AeronConfig;
import com.unisoft.algotrader.utils.networking.aeron.AeronSubscriber;
import com.unisoft.algotrader.utils.networking.aeron.AeronUtils;
import uk.co.real_logic.aeron.common.RateReporter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alex on 4/19/15.
 */
public class AeronSubscriberTest {



    public static void main(String [] args) throws Exception{
        AeronSubscriber subscriber = new AeronSubscriber(new AeronConfig.AeronConfigBuilder().build());
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
