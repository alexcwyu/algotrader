package com.unisoft.algotrader.core;

import com.unisoft.algotrader.clock.Clock;
import com.unisoft.algotrader.series.DoubleTimeSeries;

/**
 * Created by alex on 5/25/15.
 */
public class Performance {

    private final Portfolio portfolio;
    private final Clock clock;

    private boolean enabled = true;
    private DoubleTimeSeries equitySeries = new DoubleTimeSeries();
    private DoubleTimeSeries coreEquitySeries = new DoubleTimeSeries();
    private DoubleTimeSeries pnlSeries = new DoubleTimeSeries();
    private DoubleTimeSeries drawdownSeries = new DoubleTimeSeries();
    private DoubleTimeSeries drawdownPercentSeries = new DoubleTimeSeries();

    private double equity            = 0;
    private double coreEquity        = 0;
    private double lowEquity         = 0;
    private double highEquity        = 0;
    private double pnl               = 0;
    private double drawdown          = 0;
    private double drawdownPercent   = 0;
    private double currentDrawdown   = 0;
    private double currentRunUp     = 0;

    public Performance(Portfolio portfolio, Clock clock){
        this.portfolio = portfolio;
        this.clock = clock;
    }

    public void valueChanged(){
        long dateTime = clock.now();
        valueChanged(dateTime);
    }
    public void valueChanged(long dateTime){
        updateEquity(dateTime);
        updatePnl(dateTime);
        updateDrawdown(dateTime);
    }


    private void updateEquity(long dateTime){
        coreEquity = portfolio.coreEquity();
        equity = portfolio.totalEquity();
        coreEquitySeries.add(dateTime, coreEquity);
        equitySeries.add(dateTime, equity);

        if (equitySeries.count() == 1){
            highEquity = equity;
            lowEquity = equity;
        }
        else{
            if (equity > highEquity){
                highEquity = equity;
                lowEquity = equity;
                currentDrawdown = 0;
            }
            if (equity < lowEquity){
                lowEquity = equity;
                currentRunUp = 0;
            }

            if (equity > lowEquity && equity < highEquity){
                currentDrawdown = 1 - equity / highEquity;
                currentRunUp = equity / lowEquity - 1;
            }
        }
    }

    private void updatePnl(long dateTime){
        if (equitySeries.count() >=2) {
            int idx1 = equitySeries.lastIndex();
            int idx2 = idx1 -1;
            pnl = equitySeries.getByIdx(idx1) - equitySeries.getByIdx(idx2);
            pnlSeries.add(dateTime, pnl);
        }

    }

    private void updateDrawdown(long dateTime){
        if (equitySeries.count() >= 2){
            drawdown = equity - highEquity;
            drawdownSeries.add(dateTime, drawdown);

            if (highEquity != 0){
                drawdownPercent = Math.abs(drawdown / highEquity);
                drawdownPercentSeries.add(dateTime, drawdownPercent);
            }
        }
    }

    public DoubleTimeSeries getEquitySeries() {
        return equitySeries;
    }

    public DoubleTimeSeries getCoreEquitySeries() {
        return coreEquitySeries;
    }

    public DoubleTimeSeries getPnlSeries() {
        return pnlSeries;
    }

    public DoubleTimeSeries getDrawdownSeries() {
        return drawdownSeries;
    }

    public DoubleTimeSeries getDrawdownPercentSeries() {
        return drawdownPercentSeries;
    }

    public double getEquity() {
        return equity;
    }

    public double getCoreEquity() {
        return coreEquity;
    }

    public double getLowEquity() {
        return lowEquity;
    }

    public double getHighEquity() {
        return highEquity;
    }

    public double getPnl() {
        return pnl;
    }

    public double getDrawdown() {
        return drawdown;
    }

    public double getDrawdownPercent() {
        return drawdownPercent;
    }

    public double getCurrentDrawdown() {
        return currentDrawdown;
    }

    public double getCurrentRunUp() {
        return currentRunUp;
    }


}
