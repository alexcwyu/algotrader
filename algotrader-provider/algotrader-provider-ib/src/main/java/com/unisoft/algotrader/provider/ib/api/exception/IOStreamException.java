package com.unisoft.algotrader.provider.ib.api.exception;

import com.unisoft.algotrader.provider.ib.api.ClientMessageCode;

/**
 * Created by alex on 8/26/15.
 */
public class IOStreamException extends IBApiClientException {

    public IOStreamException(final ClientMessageCode clientMessageCode, final String detailedMessage,
                             final Throwable cause) {
        super(clientMessageCode, detailedMessage, cause);
    }

    public IOStreamException(final ClientMessageCode clientMessageCode, final String detailedMessage) {
        super(clientMessageCode, detailedMessage);
    }
}
