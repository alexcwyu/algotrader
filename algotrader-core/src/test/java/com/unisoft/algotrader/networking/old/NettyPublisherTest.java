package com.unisoft.algotrader.networking.old;

import com.unisoft.algotrader.networking.aeron.AeronUtils;
import com.unisoft.algotrader.networking.jeromq.JeroMQConfig;
import com.unisoft.algotrader.networking.jeromq.JeroMQPublisher;
import com.unisoft.algotrader.networking.netty.NettyPublisher;
import uk.co.real_logic.aeron.common.RateReporter;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alex on 4/19/15.
 */
public class NettyPublisherTest {

    public static void main(String [] args)throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(1);
        RateReporter reporter = AeronUtils.simpleRateReporter();
        NettyPublisher publisher = new NettyPublisher();
        executor.execute(reporter);
        publisher.connect();
        ByteBuffer buffer = ByteBuffer.allocate(4);

        boolean stop = false;
        while (!stop)
        {
            byte [] bytes = buffer.putInt(999).array();
            int length = bytes.length;
            boolean test = publisher.publish(bytes);
            if (test) {
                reporter.onMessage(1, length);
            }
            buffer.clear();
        }
    }
}
