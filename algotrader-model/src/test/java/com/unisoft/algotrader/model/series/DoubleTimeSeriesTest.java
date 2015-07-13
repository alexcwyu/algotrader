package com.unisoft.algotrader.model.series;

import com.unisoft.algotrader.model.series.DoubleTimeSeries;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

/**
 * Created by alex on 5/28/15.
 */
public class DoubleTimeSeriesTest {

    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    Calendar c3 = Calendar.getInstance();
    DoubleTimeSeries doubleTimeSeries;

    @Before
    public void setup(){
        doubleTimeSeries = new DoubleTimeSeries();
        c1.set(2000, 1, 2);
        doubleTimeSeries.add(c1.getTimeInMillis(), 11);

        c2.set(2000, 1, 3);
        doubleTimeSeries.add(c2.getTimeInMillis(), 12);

        c3.set(2000, 1, 4);
        doubleTimeSeries.add(c3.getTimeInMillis(), 13);
    }

    @Test
    public void should_return_correct_last_index(){
        Assert.assertEquals(2, doubleTimeSeries.lastIndex());
    }


    @Test
    public void should_return_correct_length(){
        Assert.assertEquals(3, doubleTimeSeries.count());
    }

    @Test
    public void should_equals_bytime_and_byidx(){
        Assert.assertEquals(doubleTimeSeries.getByDate(c1.getTimeInMillis()), doubleTimeSeries.getByIdx(0), 0.0);
        Assert.assertEquals(doubleTimeSeries.getByDate(c2.getTimeInMillis()), doubleTimeSeries.getByIdx(1), 0.0);
        Assert.assertEquals(doubleTimeSeries.getByDate(c3.getTimeInMillis()), doubleTimeSeries.getByIdx(2), 0.0);
    }

    @Test
    public void should_return_correct_ago_value(){
        Assert.assertEquals(doubleTimeSeries.ago(2), doubleTimeSeries.getByIdx(0), 0.0);
        Assert.assertEquals(doubleTimeSeries.ago(1), doubleTimeSeries.getByIdx(1), 0.0);
        Assert.assertEquals(doubleTimeSeries.ago(0), doubleTimeSeries.getByIdx(2), 0.0);
    }

    @Test
    public void should_return_correct_bydate_value(){
        Assert.assertEquals(doubleTimeSeries.getByDate(c1.getTimeInMillis()), doubleTimeSeries.getByDate(c1.getTime()), 0.0);
        Assert.assertEquals(doubleTimeSeries.getByDate(c2.getTimeInMillis()), doubleTimeSeries.getByDate(c2.getTime()), 0.0);
        Assert.assertEquals(doubleTimeSeries.getByDate(c3.getTimeInMillis()), doubleTimeSeries.getByDate(c3.getTime()), 0.0);
    }

    @Test
    public void should_return_correct_key(){
        long[] index = doubleTimeSeries.index();
        Assert.assertEquals(3, index.length);
        Assert.assertEquals(c1.getTimeInMillis(), index[0]);
        Assert.assertEquals(c2.getTimeInMillis(), index[1]);
        Assert.assertEquals(c3.getTimeInMillis(), index[2]);
    }
}
