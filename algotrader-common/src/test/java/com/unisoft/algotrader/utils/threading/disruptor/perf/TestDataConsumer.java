package com.unisoft.algotrader.utils.threading.disruptor.perf;

import java.util.function.BiConsumer;

/**
 * Created by alex on 6/13/15.
 */
class TestDataConsumer implements BiConsumer<TestData, Long> {
    private final String name;

    TestDataConsumer(String name) {
        this.name = name;
    }

    @Override
    public void accept(TestData testData, Long l) {
        testData.map.put(name, l);
    }
}
