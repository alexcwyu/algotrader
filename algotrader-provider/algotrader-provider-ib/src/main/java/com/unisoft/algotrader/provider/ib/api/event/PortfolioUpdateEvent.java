package com.unisoft.algotrader.provider.ib.api.event;


import com.unisoft.algotrader.model.refdata.Instrument;

/**
 * Created by alex on 8/26/15.
 */
public class PortfolioUpdateEvent extends IBEvent<PortfolioUpdateEvent>  {

    public final Instrument instrument;
    public final int marketPosition;
    public final double marketPrice;
    public final double marketValue;
    public final double averageCost;
    public final double unrealizedProfitAndLoss;
    public final double realizedProfitAndLoss;
    public final String accountName;

    public PortfolioUpdateEvent(final Instrument instrument, final int marketPosition, final double marketPrice,
                                final double marketValue, final double averageCost, final double unrealizedProfitAndLoss,
                                final double realizedProfitAndLoss, final String accountName){
        this.instrument = instrument;
        this.marketPosition = marketPosition;
        this.marketPrice = marketPrice;
        this.marketValue = marketValue;
        this.averageCost = averageCost;
        this.unrealizedProfitAndLoss = unrealizedProfitAndLoss;
        this.realizedProfitAndLoss = realizedProfitAndLoss;
        this.accountName = accountName;
    }

    @Override
    public void on(IBEventHandler handler) {
        handler.onPortfolioUpdateEvent(this);
    }
}