package com.unisoft.algotrader.model.series;

import com.unisoft.algotrader.model.series.DoubleTimeSeries;
import com.unisoft.algotrader.model.series.TimeSeries;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

/**
 * Created by alex on 5/28/15.
 */
public class TimeSeriesTest {

    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    Calendar c3 = Calendar.getInstance();
    TimeSeries<Double> timeSeries ;
    DoubleTimeSeries doubleTimeSeries ;

    @Before
    public void setup(){
        timeSeries = new TimeSeries();
        doubleTimeSeries = new DoubleTimeSeries();
        c1.set(2000, 1, 2);
        timeSeries.add(c1.getTimeInMillis(), 11.0);
        doubleTimeSeries.add(c1.getTimeInMillis(), 11.0);


        c2.set(2000, 1, 3);
        timeSeries.add(c2.getTimeInMillis(), 12.0);
        doubleTimeSeries.add(c2.getTimeInMillis(), 12.0);

        c3.set(2000, 1, 4);
        timeSeries.add(c3.getTimeInMillis(), 13.0);
        doubleTimeSeries.add(c3.getTimeInMillis(), 13.0);
    }

    @Test
    public void should_return_correct_last_index(){
        Assert.assertEquals(2, timeSeries.lastIndex());
    }


    @Test
    public void should_return_correct_length(){
        Assert.assertEquals(3, timeSeries.length());
    }

    @Test
    public void should_equals_bytime_and_byidx(){
        Assert.assertEquals(timeSeries.getByDate(c1.getTimeInMillis()), doubleTimeSeries.getByDate(c1.getTimeInMillis()), 0.0);
        Assert.assertEquals(timeSeries.getByDate(c2.getTimeInMillis()), doubleTimeSeries.getByDate(c2.getTimeInMillis()), 0.0);
        Assert.assertEquals(timeSeries.getByDate(c3.getTimeInMillis()), doubleTimeSeries.getByDate(c3.getTimeInMillis()), 0.0);
    }

}
