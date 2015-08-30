package com.unisoft.algotrader.provider.ib.api.exception;

import com.unisoft.algotrader.provider.ib.api.model.constants.ClientMessageCode;

/**
 * Created by alex on 8/26/15.
 */
public class ConnectionException extends IBApiClientException {

    public ConnectionException(final ClientMessageCode clientMessageCode, final String detailedMessage,
                               final Throwable cause) {
        super(clientMessageCode, detailedMessage, cause);
    }

    public ConnectionException(final ClientMessageCode clientMessageCode, final String detailedMessage) {
        super(clientMessageCode, detailedMessage);
    }
}
