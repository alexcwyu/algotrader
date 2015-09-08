package com.unisoft.algotrader.provider.ib.api.model.system;

/**
 * Created by alex on 8/30/15.
 */
public interface ErrorCode {
    // Constants
    int ORDER_INVALID_ID = 106;
    int ORDER_INCOMPLETE_ORDER = 107;
    int ORDER_PRICE_OUT_OF_RANGE = 109;
    int ORDER_INVALID_PRICE = 110;

    int HISTORICAL_MARKET_DATA_SERVICE_ERROR = 162;

    int NO_SECURITY_DEFINITION = 200;

    int MARKET_DEPTH_DATA_RESET = 317;

    int SERVER_ERROR_VALIDATING_API_REQUEST = 321;
}
