package com.unisoft.algotrader.model.trading;

import org.msgpack.annotation.OrdinalEnum;

/**
 * Created by alex on 9/18/15.
 */

@OrdinalEnum
public enum CxlRejResponseTo {
    CancelRequest,
    CancelReplaceRequest
}
