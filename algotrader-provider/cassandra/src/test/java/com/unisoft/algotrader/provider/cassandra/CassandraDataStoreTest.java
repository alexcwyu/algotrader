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
public class CassandraDataStoreTest {


    public static CassandraDataStore cassandraDataStore;

    @BeforeClass
    public static void init(){
        cassandraDataStore = new CassandraDataStore(new CassandraConfig());
        cassandraDataStore.connect();
    }

    @Before
    public void setup(){

    }

    @Test
    public void testInsertBar(){
        Bar bar1 = new Bar(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), Calendar.getInstance().getTime().getTime(), 60, 9999, 100, 500, 600);
        Bar bar2 = new Bar(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), Calendar.getInstance().getTime().getTime()+1, 60, 20000, 120, 600, 700);
        cassandraDataStore.onBar(bar1);
        cassandraDataStore.onBar(bar2);
    }
}
