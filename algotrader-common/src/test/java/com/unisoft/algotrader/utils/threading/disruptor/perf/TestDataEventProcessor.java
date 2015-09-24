package com.unisoft.algotrader.utils.threading.disruptor.perf;

import com.unisoft.algotrader.model.event.Event;

import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * Created by alex on 6/13/15.
 */
class TestDataEventProcessor implements TestDataHandler {

    private final String name;
    private final long expected;
    private final BiPredicate<TestData, Long> testFunc;
    private final BiConsumer<TestData, Long> processFunc;

    private CountDownLatch latch;
    private long totalCount = 0;

    public TestDataEventProcessor(String name, long expected, BiPredicate<TestData, Long> testFunc, BiConsumer<TestData, Long> processFunc) {
        this.name = name;
        this.expected = expected;
        this.testFunc = testFunc;
        this.processFunc = processFunc;
    }

    public void reset(final CountDownLatch latch) {
        this.latch = latch;
        this.totalCount = 0;
    }

    @Override
    public void onEvent(Event event) {
        event.on(this);
    }

    @Override
    public void onTestData(TestData data) {
        //assert testFunc.test(data, totalCount);
        //processFunc.accept(data, totalCount);

        totalCount++;
        if (totalCount >= expected) {
            //  Map<String, Object> map = Maps.newHashMap(data.map);
            //  System.out.println(name +"--"+map);
            latch.countDown();
        }
    }
}
