package com.unisoft.algotrader.provider.cassandra;

import com.unisoft.algotrader.core.id.InstId;
import com.unisoft.algotrader.event.data.Bar;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Calendar;

/**
 * Created by alex on 6/18/15.
 */
public class CassandraHistoricalDataStoreTest {


    public static CassandraHistoricalDataStore cassandraHistoricalDataStore;

    @BeforeClass
    public static void init(){
        cassandraHistoricalDataStore = new CassandraHistoricalDataStore(new CassandraConfig());
        cassandraHistoricalDataStore.connect();
    }

    @Before
    public void setup(){

    }

    @Test
    public void testInsertBar(){
        Bar bar1 = new Bar(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(),  60,Calendar.getInstance().getTime().getTime(),
                500, 9999, 100, 600);
        Bar bar2 = new Bar(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(),  60, Calendar.getInstance().getTime().getTime()+1,
                600, 20000, 120, 700);
        cassandraHistoricalDataStore.onBar(bar1);
        cassandraHistoricalDataStore.onBar(bar2);
    }
}
