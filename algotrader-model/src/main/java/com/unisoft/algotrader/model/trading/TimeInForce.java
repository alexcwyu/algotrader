package com.unisoft.algotrader.model.trading;

import org.msgpack.annotation.OrdinalEnum;

/**
 * Created by alex on 5/21/15.
 */
@OrdinalEnum
public enum TimeInForce {
    Undefined,
    Day,
    GTC,
    OPG,
    IOC,
    FOK,
    GTX,
    GoodTillDate,
    AtTheClose
}
