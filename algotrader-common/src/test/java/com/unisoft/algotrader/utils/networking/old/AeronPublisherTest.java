package com.unisoft.algotrader.utils.networking.old;

import com.unisoft.algotrader.utils.networking.aeron.AeronConfig;
import com.unisoft.algotrader.utils.networking.aeron.AeronPublisher;
import com.unisoft.algotrader.utils.networking.aeron.AeronUtils;
import uk.co.real_logic.aeron.driver.RateReporter;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alex on 4/19/15.
 */
public class AeronPublisherTest {

    public static void main(String [] args) throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(1);
        RateReporter reporter = AeronUtils.simpleRateReporter();
        AeronPublisher publisher = new AeronPublisher(new AeronConfig.AeronConfigBuilder().build());
        publisher.connect();

        executor.execute(reporter);
        ByteBuffer buffer = ByteBuffer.allocate(4);

        boolean stop = false;
        while (!stop)
        {
            byte [] bytes = buffer.putInt(999).array();
            int length = bytes.length;
            publisher.publish(bytes);
            reporter.onMessage(1, length);
            buffer.clear();
        }
    }
}
