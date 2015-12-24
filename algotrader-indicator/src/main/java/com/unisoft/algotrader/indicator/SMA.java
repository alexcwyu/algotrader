package com.unisoft.algotrader.indicator;

import com.unisoft.algotrader.model.series.DoubleTimeSeries;

/**
 * Created by alex on 12/20/15.
 */
public class SMA extends Indicator {

    protected int length;

    protected static final String ID = "SMA";
    protected static final String DESC = "Simple Moving Average";

    public SMA(DoubleTimeSeries input, int length) {
        super(ID, DESC, input);
        this.length = length;
        this.name = ID + "("+input.getName()+", "+length+")";
    }

    @Override
    public void calculate(int index) {
        double sma = value(input, index, length);
        add(input.getDateTime(index), sma);
    }


    public static double value(DoubleTimeSeries input, int index, int length){
        if (index >= length -1 + input.firstIndex()) {
            double sma = 0;
            for (int i = index; i >= index - length + 1; i--) {
                sma += input.getByIdx(i);
            }
            sma /= length;
            return sma;
        }
        return Double.NaN;

    }
}
