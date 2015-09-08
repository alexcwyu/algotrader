package com.unisoft.algotrader.provider.ib.api.exception;

import com.unisoft.algotrader.provider.ib.api.model.system.ClientMessageCode;

/**
 * Created by alex on 8/26/15.
 */
public class SessionException extends IBApiClientException{

    public SessionException(final ClientMessageCode clientMessageCode, final String detailedMessage) {
        super(clientMessageCode, detailedMessage);
    }

    public SessionException(final ClientMessageCode clientMessageCode, final String detailedMessage,
                            final Throwable cause) {
        super(clientMessageCode, detailedMessage, cause);
    }
}
