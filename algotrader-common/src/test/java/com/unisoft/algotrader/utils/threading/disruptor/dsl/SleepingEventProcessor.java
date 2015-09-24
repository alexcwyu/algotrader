package com.unisoft.algotrader.utils.threading.disruptor.dsl;

import com.unisoft.algotrader.model.event.EventHandler;
import com.unisoft.algotrader.utils.threading.disruptor.MultiEventProcessor;

/**
 * Created by alex on 6/11/15.
 */
public class SleepingEventProcessor extends MultiEventProcessor {
    public SleepingEventProcessor(EventHandler eventHandler) {
        super(eventHandler);
    }

    //    @Override
//    public void onEvent(Event event) {
//        try {
//            Thread.sleep(1000);
//        }
//        catch (Exception e){}
//    }
}
