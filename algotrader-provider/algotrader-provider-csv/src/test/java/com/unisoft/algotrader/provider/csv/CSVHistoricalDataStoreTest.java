package com.unisoft.algotrader.provider.csv;

import com.unisoft.algotrader.event.data.Bar;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 6/16/15.
 */
public class CSVHistoricalDataStoreTest {

    String EXPECTED = "Date,Open,High,Low,Close,Volume,OpenInt\n"+
            "19999,500.0,9999.0,100.0,600.0,0,0\n"+
            "20000,600.0,20000.0,120.0,700.0,0,0\n";

    @Test
    public void testCsvImport(){
        StringWriter sw = new StringWriter();
        CSVHistoricalDataStore csvImport = new CSVHistoricalDataStore(sw);

        Bar bar1 = new Bar(1,  60, 19999,
                500, 9999, 100, 600);
        Bar bar2 = new Bar(1,  60, 20000,
                600, 20000, 120, 700);

        csvImport.onBar(bar1);
        csvImport.onBar(bar2);
        csvImport.disconnect();

        assertEquals(EXPECTED, sw.toString());

    }
}
