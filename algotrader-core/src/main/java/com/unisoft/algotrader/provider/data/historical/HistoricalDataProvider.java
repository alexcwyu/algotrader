package com.unisoft.algotrader.provider.data.historical;

import com.unisoft.algotrader.provider.Provider;

/**
 * Created by alex on 5/17/15.
 */
public interface HistoricalDataProvider extends Provider{

    public void subscribe(String instId, int fromDate, int toDate);

}
