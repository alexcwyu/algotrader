package com.unisoft.algotrader.model.trading;

import org.msgpack.annotation.OrdinalEnum;

/**
 * Created by alex on 5/21/15.
 */
@OrdinalEnum
public enum Side
{
    Undefined,
    Buy,
    Sell,
    BuyMinus,
    SellPlus,
    SellShort,
    SellShortExempt,
    Undisclosed,
    Cross,
    CrossShort,
    CrossShortExempt,
    AsDefined,
    Opposite,
}