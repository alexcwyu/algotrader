package com.unisoft.algotrader.indicator;

import com.unisoft.algotrader.model.series.DoubleTimeSeries;

/**
 * Created by alex on 12/22/15.
 */
public class SMATest {

    public static final int TOTAL_PERIODS = 100;
    public static final int PERIODS_AVERAGE = 30;

    public static void main(String [] args){
        DoubleTimeSeries series = new DoubleTimeSeries("HSI");

        for (int i = 0; i < TOTAL_PERIODS; i++) {
            series.add(i, i);
        }

        SMA sma = new SMA(series, 30);

        SMA sma2 = new SMA(sma, 2);
        sma2.calculate();



        System.out.println(sma.getName());
        System.out.println(sma2.getName());
        for (int i = 0; i < TOTAL_PERIODS; i++) {
            System.out.println(i+" - "+sma.getByIdx(i)+" - "+sma2.getByIdx(i));
        }
    }
}
