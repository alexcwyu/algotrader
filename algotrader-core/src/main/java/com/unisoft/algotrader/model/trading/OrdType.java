package com.unisoft.algotrader.model.trading;

import org.msgpack.annotation.OrdinalEnum;

/**
 * Created by alex on 5/21/15.
 */
@OrdinalEnum
public enum OrdType {
    Undefined,
    Market,
    Limit,
    Stop,
    StopLimit,
    MarketOnClose,
    WithOrWithout,
    LimitOrBetter,
    LimitWithOrWithout,
    OnBasis,
    OnClose,
    LimitOnClose,
    ForexMarket,
    PreviouslyQuoted,
    PreviouslyIndicated,
    ForexLimit,
    ForexSwap,
    ForexPreviouslyQuoted,
    Funari,
    MIT,
    MarketWithLeftoverAsLimit,
    PreviousFundValuationPoint,
    NextFundValuationPoint,
    Pegged,
    TrailingStop // non-standard getValue
}
