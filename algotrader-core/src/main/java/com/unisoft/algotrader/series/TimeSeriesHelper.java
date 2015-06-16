package com.unisoft.algotrader.series;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alex on 6/16/15.
 */
public class TimeSeriesHelper {

    public static String PATTERN = "yyyyMMdd";
    public static SimpleDateFormat FORMAT = new SimpleDateFormat(PATTERN);


    public static String print(DoubleTimeSeries timeSeries, DateFormat format){
        StringBuilder sb = new StringBuilder();
        for (DoubleTimeSeries.Tuple tuple : timeSeries){
            Date date = new Date(tuple.dateTime);
            sb.append(format.format(date));
            sb.append(" -- ");
            sb.append(tuple.data);
            sb.append("\n");
        }

        return sb.toString();
    }

    public static String print(DoubleTimeSeries timeSeries){
        return print(timeSeries, FORMAT);
    }
}
