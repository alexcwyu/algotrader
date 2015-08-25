package com.unisoft.algotrader.provider.ib.api.exception;

import com.unisoft.algotrader.provider.ib.api.ClientMessageCode;

/**
 * Created by alex on 8/26/15.
 */
public class ConsumerException extends IOStreamException {

    public ConsumerException(final ClientMessageCode clientMessageCode, final String detailedMessage,
                             final Throwable cause) {
        super(clientMessageCode, detailedMessage, cause);
    }

    public ConsumerException(final ClientMessageCode clientMessageCode, final String detailedMessage) {
        super(clientMessageCode, detailedMessage);
    }
}
