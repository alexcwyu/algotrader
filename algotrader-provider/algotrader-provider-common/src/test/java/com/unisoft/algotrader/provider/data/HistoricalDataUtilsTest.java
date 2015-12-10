package com.unisoft.algotrader.provider.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.unisoft.algotrader.model.event.data.MarketDataContainer;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 12/8/15.
 */
public class HistoricalDataUtilsTest {

    @Test
    public void should_sort(){
        Map<HistoricalSubscriptionKey, List<MarketDataContainer>> data = Maps.newHashMap();

        MarketDataContainer m1 = new MarketDataContainer();
        m1.dateTime=1;

        MarketDataContainer m2 = new MarketDataContainer();
        m2.dateTime=2;

        MarketDataContainer m3 = new MarketDataContainer();
        m3.dateTime=3;

        MarketDataContainer m4 = new MarketDataContainer();
        m4.dateTime=4;

        MarketDataContainer m5 = new MarketDataContainer();
        m5.dateTime=5;

        MarketDataContainer m6 = new MarketDataContainer();
        m6.dateTime=6;

        List<MarketDataContainer> l1 = Lists.newArrayList(m5, m1, m3);

        List<MarketDataContainer> l2 = Lists.newArrayList(m6, m4);

        List<MarketDataContainer> l3 = Lists.newArrayList(m2);

        data.put(HistoricalSubscriptionKey.createDailySubscriptionKey(1, 1, 0, 7), l1);
        data.put(HistoricalSubscriptionKey.createDailySubscriptionKey(1, 2, 0, 7), l2);
        data.put(HistoricalSubscriptionKey.createDailySubscriptionKey(1, 3, 0, 7), l3);

        List<MarketDataContainer> expected = Lists.newArrayList(m1, m2, m3, m4, m5, m6);

        List<MarketDataContainer> result = HistoricalDataUtils.sortedHistoricalData(data);

        assertEquals(expected, result);
    }

}


