package com.unisoft.algotrader.indicator;

import com.unisoft.algotrader.model.series.DoubleTimeSeries;

import java.util.Date;

/**
 * Created by alex on 12/20/15.
 */
public abstract class Indicator extends DoubleTimeSeries {

    private final String id;
    private final String description;

    protected DoubleTimeSeries input;

    protected boolean calculate = true;


    public Indicator(String id, String description, DoubleTimeSeries input){
        this.id = id;
        this.description = description;
        this.input = input;
    }


    public void calculate(){
        calculate(false);
    }

    public void calculate(boolean force){
        if (force || calculate){
            calculate = false;

            if (input instanceof Indicator){
                ((Indicator) input).calculate();
            }

            for (int index = 0; index < input.count(); index ++){
                calculate(index);
            }
        }
    }

    public abstract void calculate(int index);

    public void add(Date date, double value){
        add(date.getTime(), value);
    }

    public void add(long dateTime, double value){
        super.add(dateTime, value);
    }

//    public void remove(){
//        //TODO
//    }
//
//    public void clear(){
//
//    }


    public String id(){
        return this.id;
    }

    public String description(){
        return this.description;
    }


}
