package com.unisoft.algotrader.model.trading;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.Frozen;
import com.datastax.driver.mapping.annotations.UDT;
import com.google.common.base.Objects;
import com.unisoft.algotrader.model.series.DoubleTimeSeries;

/**
 * Created by alex on 5/25/15.
 */

@UDT(name = "performance", keyspace = "trading")
public class Performance {

    private boolean enabled = true;

    private double equity            = 0;

    @Field(name = "core_equity")
    private double coreEquity        = 0;

    @Field(name = "low_equity")
    private double lowEquity         = 0;

    @Field(name = "high_equity")
    private double highEquity        = 0;
    private double pnl               = 0;
    private double drawdown          = 0;

    @Field(name = "drawdown_percent")
    private double drawdownPercent   = 0;

    @Field(name = "current_drawdown")
    private double currentDrawdown   = 0;

    @Field(name = "current_run_up")
    private double currentRunUp     = 0;

    @Field(name = "equity_series")
    @Frozen
    private DoubleTimeSeries equitySeries = new DoubleTimeSeries();

    @Field(name = "core_equity_series")
    @Frozen
    private DoubleTimeSeries coreEquitySeries = new DoubleTimeSeries();

    @Field(name = "pnl_series")
    @Frozen
    private DoubleTimeSeries pnlSeries = new DoubleTimeSeries();

    @Field(name = "drawdown_series")
    @Frozen
    private DoubleTimeSeries drawdownSeries = new DoubleTimeSeries();

    @Field(name = "drawdown_percent_series")
    @Frozen
    private DoubleTimeSeries drawdownPercentSeries = new DoubleTimeSeries();

    public Performance(){}


    public void valueChanged(long dateTime, double coreEquity, double equity){
        updateEquity(dateTime, coreEquity, equity);
        updatePnl(dateTime);
        updateDrawdown(dateTime);
    }


    private void updateEquity(long dateTime, double coreEquity, double equity){
        this.coreEquity = coreEquity;
        this.equity = equity;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Performance)) return false;
        Performance that = (Performance) o;
        return Objects.equal(enabled, that.enabled) &&
                Objects.equal(equity, that.equity) &&
                Objects.equal(coreEquity, that.coreEquity) &&
                Objects.equal(lowEquity, that.lowEquity) &&
                Objects.equal(highEquity, that.highEquity) &&
                Objects.equal(pnl, that.pnl) &&
                Objects.equal(drawdown, that.drawdown) &&
                Objects.equal(drawdownPercent, that.drawdownPercent) &&
                Objects.equal(currentDrawdown, that.currentDrawdown) &&
                Objects.equal(currentRunUp, that.currentRunUp) &&
                Objects.equal(equitySeries, that.equitySeries) &&
                Objects.equal(coreEquitySeries, that.coreEquitySeries) &&
                Objects.equal(pnlSeries, that.pnlSeries) &&
                Objects.equal(drawdownSeries, that.drawdownSeries) &&
                Objects.equal(drawdownPercentSeries, that.drawdownPercentSeries);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(enabled, equity, coreEquity, lowEquity, highEquity, pnl, drawdown, drawdownPercent, currentDrawdown, currentRunUp, equitySeries, coreEquitySeries, pnlSeries, drawdownSeries, drawdownPercentSeries);
    }

    @Override
    public String toString() {
        return "Performance{" +
                "enabled=" + enabled +
//                ", equitySeries=" + equitySeries +
//                ", coreEquitySeries=" + coreEquitySeries +
//                ", pnlSeries=" + pnlSeries +
//                ", drawdownSeries=" + drawdownSeries +
//                ", drawdownPercentSeries=" + drawdownPercentSeries +
                ", equity=" + equity +
                ", coreEquity=" + coreEquity +
                ", lowEquity=" + lowEquity +
                ", highEquity=" + highEquity +
                ", pnl=" + pnl +
                ", drawdown=" + drawdown +
                ", drawdownPercent=" + drawdownPercent +
                ", currentDrawdown=" + currentDrawdown +
                ", currentRunUp=" + currentRunUp +
                "} " + super.toString();
    }


    public double coreEquity() {
        return coreEquity;
    }

    public Performance coreEquity(double coreEquity) {
        this.coreEquity = coreEquity;
        return this;
    }

    public DoubleTimeSeries coreEquitySeries() {
        return coreEquitySeries;
    }

    public Performance coreEquitySeries(DoubleTimeSeries coreEquitySeries) {
        this.coreEquitySeries = coreEquitySeries;
        return this;
    }

    public double currentDrawdown() {
        return currentDrawdown;
    }

    public Performance currentDrawdown(double currentDrawdown) {
        this.currentDrawdown = currentDrawdown;
        return this;
    }

    public double currentRunUp() {
        return currentRunUp;
    }

    public Performance currentRunUp(double currentRunUp) {
        this.currentRunUp = currentRunUp;
        return this;
    }

    public double drawdown() {
        return drawdown;
    }

    public Performance drawdown(double drawdown) {
        this.drawdown = drawdown;
        return this;
    }

    public double drawdownPercent() {
        return drawdownPercent;
    }

    public Performance drawdownPercent(double drawdownPercent) {
        this.drawdownPercent = drawdownPercent;
        return this;
    }

    public DoubleTimeSeries drawdownPercentSeries() {
        return drawdownPercentSeries;
    }

    public Performance drawdownPercentSeries(DoubleTimeSeries drawdownPercentSeries) {
        this.drawdownPercentSeries = drawdownPercentSeries;
        return this;
    }

    public DoubleTimeSeries drawdownSeries() {
        return drawdownSeries;
    }

    public Performance drawdownSeries(DoubleTimeSeries drawdownSeries) {
        this.drawdownSeries = drawdownSeries;
        return this;
    }

    public boolean enabled() {
        return enabled;
    }

    public Performance enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public double equity() {
        return equity;
    }

    public Performance equity(double equity) {
        this.equity = equity;
        return this;
    }

    public DoubleTimeSeries equitySeries() {
        return equitySeries;
    }

    public Performance equitySeries(DoubleTimeSeries equitySeries) {
        this.equitySeries = equitySeries;
        return this;
    }

    public double highEquity() {
        return highEquity;
    }

    public Performance highEquity(double highEquity) {
        this.highEquity = highEquity;
        return this;
    }

    public double lowEquity() {
        return lowEquity;
    }

    public Performance lowEquity(double lowEquity) {
        this.lowEquity = lowEquity;
        return this;
    }

    public double pnl() {
        return pnl;
    }

    public Performance pnl(double pnl) {
        this.pnl = pnl;
        return this;
    }

    public DoubleTimeSeries pnlSeries() {
        return pnlSeries;
    }

    public Performance pnlSeries(DoubleTimeSeries pnlSeries) {
        this.pnlSeries = pnlSeries;
        return this;
    }
}
