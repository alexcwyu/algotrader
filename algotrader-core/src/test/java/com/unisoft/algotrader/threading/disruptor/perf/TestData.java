package com.unisoft.algotrader.threading.disruptor.perf;

import com.lmax.disruptor.EventFactory;
import com.unisoft.algotrader.model.event.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 6/13/15.
 */
class TestData implements Event<TestDataHandler, TestData> {

    public Map<String, Object> map = new HashMap<>();

    @Override
    public void on(TestDataHandler handler) {
        handler.onTestData(this);
    }

    @Override
    public void reset() {
        map.clear();
    }

    @Override
    public void copy(TestData event) {

    }

    public static final EventFactory<TestData> FACTORY = new EventFactory() {
        @Override
        public TestData newInstance() {
            return new TestData();
        }
    };
}
