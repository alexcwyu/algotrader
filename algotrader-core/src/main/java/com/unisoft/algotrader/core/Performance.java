package com.unisoft.algotrader.core;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.Frozen;
import com.datastax.driver.mapping.annotations.Transient;
import com.datastax.driver.mapping.annotations.UDT;
import com.google.common.base.Objects;
import com.unisoft.algotrader.clock.Clock;
import com.unisoft.algotrader.series.DoubleTimeSeries;
import org.msgpack.annotation.Ignore;

/**
 * Created by alex on 5/25/15.
 */

@UDT(name = "performance", keyspace = "trading")
public class Performance {

    @Field(name = "portfolio_id")
    private String portfolioId;

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

    @Ignore
    @Transient
    private Portfolio portfolio;

    @Ignore
    @Transient
    private Clock clock;

    public Performance(){}

    public Performance(Portfolio portfolio, Clock clock){
        this.portfolio = portfolio;
        this.portfolioId = portfolio.getPortfolioId();
        this.clock = clock;
    }


    public Performance(String portfolioId){
        this.portfolioId = portfolioId;
    }


    private Portfolio getPortfolio(){
        if (portfolio == null){
            portfolio = PortfolioManager.INSTANCE.get(portfolioId);
        }
        return portfolio;
    }


    private Clock getClock(){
        if (clock == null){
            clock = Clock.CLOCK;
        }
        return clock;
    }

    public void valueChanged(){
        long dateTime = getClock().now();
        valueChanged(dateTime);
    }
    public void valueChanged(long dateTime){
        updateEquity(dateTime);
        updatePnl(dateTime);
        updateDrawdown(dateTime);
    }


    private void updateEquity(long dateTime){
        Portfolio portfolio = getPortfolio();
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
                Objects.equal(portfolioId, that.portfolioId) &&
                Objects.equal(equitySeries, that.equitySeries) &&
                Objects.equal(coreEquitySeries, that.coreEquitySeries) &&
                Objects.equal(pnlSeries, that.pnlSeries) &&
                Objects.equal(drawdownSeries, that.drawdownSeries) &&
                Objects.equal(drawdownPercentSeries, that.drawdownPercentSeries);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(portfolioId, enabled, equity, coreEquity, lowEquity, highEquity, pnl, drawdown, drawdownPercent, currentDrawdown, currentRunUp, equitySeries, coreEquitySeries, pnlSeries, drawdownSeries, drawdownPercentSeries);
    }

    @Override
    public String toString() {
        return "Performance{" +
                "portfolioId='" + portfolioId + '\'' +
                ", enabled=" + enabled +
                ", equitySeries=" + equitySeries +
                ", coreEquitySeries=" + coreEquitySeries +
                ", pnlSeries=" + pnlSeries +
                ", drawdownSeries=" + drawdownSeries +
                ", drawdownPercentSeries=" + drawdownPercentSeries +
                ", equity=" + equity +
                ", coreEquity=" + coreEquity +
                ", lowEquity=" + lowEquity +
                ", highEquity=" + highEquity +
                ", pnl=" + pnl +
                ", drawdown=" + drawdown +
                ", drawdownPercent=" + drawdownPercent +
                ", currentDrawdown=" + currentDrawdown +
                ", currentRunUp=" + currentRunUp +
                ", portfolio=" + portfolio +
                ", clock=" + clock +
                '}';
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEquitySeries(DoubleTimeSeries equitySeries) {
        this.equitySeries = equitySeries;
    }

    public void setCoreEquitySeries(DoubleTimeSeries coreEquitySeries) {
        this.coreEquitySeries = coreEquitySeries;
    }

    public void setPnlSeries(DoubleTimeSeries pnlSeries) {
        this.pnlSeries = pnlSeries;
    }

    public void setDrawdownSeries(DoubleTimeSeries drawdownSeries) {
        this.drawdownSeries = drawdownSeries;
    }

    public void setDrawdownPercentSeries(DoubleTimeSeries drawdownPercentSeries) {
        this.drawdownPercentSeries = drawdownPercentSeries;
    }

    public void setEquity(double equity) {
        this.equity = equity;
    }

    public void setCoreEquity(double coreEquity) {
        this.coreEquity = coreEquity;
    }

    public void setLowEquity(double lowEquity) {
        this.lowEquity = lowEquity;
    }

    public void setHighEquity(double highEquity) {
        this.highEquity = highEquity;
    }

    public void setPnl(double pnl) {
        this.pnl = pnl;
    }

    public void setDrawdownPercent(double drawdownPercent) {
        this.drawdownPercent = drawdownPercent;
    }

    public void setDrawdown(double drawdown) {
        this.drawdown = drawdown;
    }

    public void setCurrentDrawdown(double currentDrawdown) {
        this.currentDrawdown = currentDrawdown;
    }

    public void setCurrentRunUp(double currentRunUp) {
        this.currentRunUp = currentRunUp;
    }
}
