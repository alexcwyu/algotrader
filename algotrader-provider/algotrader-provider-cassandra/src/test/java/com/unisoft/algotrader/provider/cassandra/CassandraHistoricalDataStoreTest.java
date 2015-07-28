package com.unisoft.algotrader.provider.cassandra;

import com.unisoft.algotrader.model.event.EventBus;
import com.unisoft.algotrader.model.event.data.Bar;
import com.unisoft.algotrader.provider.ProviderManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Calendar;

import static org.mockito.Mockito.mock;

/**
 * Created by alex on 6/18/15.
 */
public class CassandraHistoricalDataStoreTest {


    public static CassandraHistoricalDataStore cassandraHistoricalDataStore;
    public static ProviderManager providerManager;
    @BeforeClass
    public static void init(){
        providerManager = new ProviderManager();
        cassandraHistoricalDataStore = new CassandraHistoricalDataStore(providerManager, new CassandraHistoricalDataStoreConfig("127.0.0.1", 0, "marketdata", null, null), mock(EventBus.MarketDataEventBus.class));
        cassandraHistoricalDataStore.connect();
    }

    @Before
    public void setup(){

    }

    @Test
    public void testInsertBar(){
        Bar bar1 = new Bar(1,  60,Calendar.getInstance().getTime().getTime(),
                500, 9999, 100, 600);
        Bar bar2 = new Bar(1,  60, Calendar.getInstance().getTime().getTime()+1,
                600, 20000, 120, 700);
        cassandraHistoricalDataStore.onBar(bar1);
        cassandraHistoricalDataStore.onBar(bar2);
    }
}
