package com.unisoft.algotrader.threading.disruptor.perf;

import com.unisoft.algotrader.model.event.EventHandler;

/**
 * Created by alex on 6/13/15.
 */
interface TestDataHandler extends EventHandler {

    void onTestData(TestData data);

}
