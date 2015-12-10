package com.unisoft.algotrader.provider.data;

import com.unisoft.algotrader.model.event.data.MarketDataContainer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by alex on 12/8/15.
 */
public class HistoricalDataUtils {

    public static List<MarketDataContainer> sortedHistoricalData(Map<HistoricalSubscriptionKey, List<MarketDataContainer>> data){
        return data.values().stream().flatMap(List::stream).sorted((o1, o2) -> Long.compare(o1.dateTime, o2.dateTime)).collect(Collectors.toList());
    }
}
