package com.unisoft.algotrader.model.series;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;
import com.google.common.base.Objects;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * Created by alex on 5/25/15.
 */

@UDT(name = "double_time_series", keyspace = "trading")
public class DoubleTimeSeries implements Iterable<DoubleTimeSeries.Tuple>{


    public static class Tuple{
        public final long dateTime;
        public final double data;

        Tuple(long dateTime , double data){
            this.dateTime = dateTime;
            this.data = data;
        }

        @Override
        public String toString() {
            return "Tuple{" +
                    "dateTime=" + dateTime +
                    ", data=" + data +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Tuple)) return false;
            Tuple tuple = (Tuple) o;
            return Objects.equal(dateTime, tuple.dateTime) &&
                    Objects.equal(data, tuple.data);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(dateTime, data);
        }
    }

    public static class DoubleTimeSeriesIterator implements Iterator<DoubleTimeSeries.Tuple>{
        private final long [] dateTimeSeries;
        private final double [] dataSeries;
        private int cursor;

        public DoubleTimeSeriesIterator(long [] dateTimeSeries, double [] dataSeries){
            this.dateTimeSeries = dateTimeSeries;
            this.dataSeries = dataSeries;
            cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return cursor < dateTimeSeries.length;
        }

        @Override
        public Tuple next() {
            if (hasNext()) {
                Tuple tuple = new Tuple(dateTimeSeries[cursor], dataSeries[cursor]);
                cursor++;
                return tuple;
            }
            throw new NoSuchElementException();
        }
    }

//
//    private TLongArrayList datetimeSeries = new TLongArrayList();
//    private TLongIntHashMap indexMap = new TLongIntHashMap();
//    private TDoubleArrayList dataSeries = new TDoubleArrayList();


    @Field(name = "datetime_series")
    private List<Long> datetimeSeries = new ArrayList();

    @Field(name = "index_map")
    private Map<Long, Integer> indexMap = new HashMap();

    @Field(name = "data_series")
    private List<Double> dataSeries = new ArrayList();

    @Field(name = "curr_idx")
    private int currIdx = 0;

    @Field(name = "curr_time")
    private long currTime = Long.MIN_VALUE;

    public void add(Date date, double data){
        add(date.getTime(), data);
    }

    public void add(long date, double data){
        assert date >= currTime;
        currTime = date;
        indexMap.put(date, currIdx++);
        dataSeries.add(data);
        datetimeSeries.add(date);

    }

    public int count(){
        return dataSeries.size();
    }


    public double getByIdx(int idx){
        return dataSeries.get(idx);
    }

    public double getByDate(Date date){
        return getByDate(date.getTime());
    }

    public double getByDate(long datetime){
        int index = getIndex(datetime);
        return index>=0 ? dataSeries.get(index) : 0.0;
    }

    protected int getIndex(long datetime){
        return indexMap.containsKey(datetime) ? indexMap.get(datetime) : -1;
    }

    public double ago(int ago){
        int index = lastIndex() - ago;
        return index>=0 ? dataSeries.get(index) : 0.0;
    }

    public long [] index(){
        List<Long> idxList = new ArrayList<>(indexMap.keySet());
        long[] idx = ArrayUtils.toPrimitive(idxList.toArray(new Long[idxList.size()]));
       //long [] idx =indexMap.keySet().toArray();
        Arrays.sort(idx);
        return idx;
    }

    public long firstDateTime(){
        if (count() <= 0){
            throw new IllegalArgumentException("Time Series has no element");
        }
        return datetimeSeries.get(0);
    }

    public long lastDateTime(){
        if (count() <= 0){
            throw new IllegalArgumentException("Time Series has no element");
        }
        return datetimeSeries.get(count()-1);
    }

    public int firstIndex(){
        return 0;
    }

    public int lastIndex(){
        return dataSeries.size()-1;
    }

    public double first(){
        if (count() <= 0){
            throw new IllegalArgumentException("Time Series has no element");
        }
        return dataSeries.get(0);
    }

    public double last(){
        if (count() <= 0){
            throw new IllegalArgumentException("Time Series has no element");
        }
        return dataSeries.get(count()-1);
    }


    public Iterator<Tuple> iterator() {
        long[] date = ArrayUtils.toPrimitive(datetimeSeries.toArray(new Long[datetimeSeries.size()]));
        double[] data = ArrayUtils.toPrimitive(dataSeries.toArray(new Double[dataSeries.size()]));
        return new DoubleTimeSeriesIterator(date, data);

        //return new DoubleTimeSeriesIterator(datetimeSeries.toArray(), dataSeries.toArray());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleTimeSeries)) return false;
        DoubleTimeSeries tuples = (DoubleTimeSeries) o;
        return Objects.equal(currIdx, tuples.currIdx) &&
                Objects.equal(currTime, tuples.currTime) &&
                Objects.equal(datetimeSeries, tuples.datetimeSeries) &&
                Objects.equal(indexMap, tuples.indexMap) &&
                Objects.equal(dataSeries, tuples.dataSeries);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(datetimeSeries, indexMap, dataSeries, currIdx, currTime);
    }

    public List<Long> getDatetimeSeries() {
        return datetimeSeries;
    }

    public void setDatetimeSeries(List<Long> datetimeSeries) {
        this.datetimeSeries = datetimeSeries;
    }

    public Map<Long, Integer> getIndexMap() {
        return indexMap;
    }

    public void setIndexMap(Map<Long, Integer> indexMap) {
        this.indexMap = indexMap;
    }

    public List<Double> getDataSeries() {
        return dataSeries;
    }

    public void setDataSeries(List<Double> dataSeries) {
        this.dataSeries = dataSeries;
    }

    public int getCurrIdx() {
        return currIdx;
    }

    public void setCurrIdx(int currIdx) {
        this.currIdx = currIdx;
    }

    public long getCurrTime() {
        return currTime;
    }

    public void setCurrTime(long currTime) {
        this.currTime = currTime;
    }
}
