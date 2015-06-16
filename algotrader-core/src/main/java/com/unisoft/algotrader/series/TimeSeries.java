package com.unisoft.algotrader.series;

import gnu.trove.map.TLongIntMap;
import gnu.trove.map.hash.TLongIntHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 5/25/15.
 */
public class TimeSeries<T> {


    //private TLongList datetimeSeries = new TLongArrayList();
    private final TLongIntMap index = new TLongIntHashMap();
    private final List<T> dataSeries;
    public final T nullValue;

    private int currIdx = 0;
    private long currTime = Long.MIN_VALUE;


    public TimeSeries(List<T> dataSeries, T nullValue){
        this.dataSeries = dataSeries;
        this.nullValue = nullValue;
    }

    public TimeSeries(T nullValue){
        this(new ArrayList<>(), nullValue);
    }

    public TimeSeries(List<T> dataSeries){
        this(dataSeries, null);
    }

    public TimeSeries(){
        this(new ArrayList<>(), null);
    }


    public void add(Date date, T data){
        add(date.getTime(), data);
    }

    public void add(long date, T data){
        assert date > currTime;
        currTime = date;
        index.put(date, currIdx++);
        dataSeries.add(data);
    }

    public int length(){
        return dataSeries.size();
    }

    public int lastIndex(){
        return dataSeries.size()-1;
    }

    public T getByIdx(int idx){
        return dataSeries.get(idx);
    }

    public T getByDate(Date date){
        return getByDate(date.getTime());
    }

    public T getByDate(long datetime){
        int index = getIndex(datetime);
        return index>=0 ? dataSeries.get(index) : nullValue;
    }

    protected int getIndex(long datetime){
        return index.containsKey(datetime) ? index.get(datetime) : -1;
    }

    public T ago(int ago){
        int index = lastIndex() - ago;
        return index>=0 ? dataSeries.get(index) : nullValue;
    }

    public long [] index(){
        long [] idx =index.keys();
        Arrays.sort(idx);
        return idx;
    }
}
