package com.unisoft.algotrader.provider.execution.simulation;

/**
 * Created by alex on 6/6/15.
 */
public class SimulatorConfig {

    public enum FillOnQuoteMode
    {
        LastQuote,
        NextQuote,
    }

    public enum FillOnBarMode
    {
        LastBarClose,
        NextBarOpen,
        //        NextBarBest,
//        NextBarWorst,
        NextBarClose
    }

    public enum FillOnTradeMode
    {
        LastTrade,
        NextTrade,
    }

    public boolean partialFills = true;
    public boolean fillOnQuote = true;
    public boolean fillOnTrade = true;
    public boolean fillOnBar = true;
    public FillOnQuoteMode fillOnQuoteMode = FillOnQuoteMode.LastQuote;
    public FillOnBarMode fillOnBarMode = FillOnBarMode.LastBarClose;
    public FillOnTradeMode fillOnTradeMode = FillOnTradeMode.LastTrade;

}
