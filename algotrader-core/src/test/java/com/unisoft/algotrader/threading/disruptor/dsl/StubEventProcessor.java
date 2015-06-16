package com.unisoft.algotrader.threading.disruptor.dsl;

import com.unisoft.algotrader.event.Event;
import com.unisoft.algotrader.threading.MultiEventProcessor;

import java.util.concurrent.CountDownLatch;

/**
 * Created by alex on 6/12/15.
 */
public class StubEventProcessor extends MultiEventProcessor {
    private final CountDownLatch countDownLatch;

    public StubEventProcessor(final CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onEvent(Event event) {
        try {
            countDownLatch.countDown();
        } catch (Exception e) {
        }
    }
}
