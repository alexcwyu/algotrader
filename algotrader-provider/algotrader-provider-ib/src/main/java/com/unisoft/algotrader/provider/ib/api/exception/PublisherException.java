package com.unisoft.algotrader.provider.ib.api.exception;

import com.unisoft.algotrader.provider.ib.api.model.constants.ClientMessageCode;

/**
 * Created by alex on 8/26/15.
 */
public class PublisherException extends IOStreamException {

    private final String requestId;

    public PublisherException(final ClientMessageCode clientMessageCode, final String detailedMessage,
                              final String requestId, final Throwable cause) {
        super(clientMessageCode, detailedMessage, cause);
        this.requestId = requestId;
    }

    public PublisherException(final ClientMessageCode clientMessageCode, final String detailedMessage,
                              final String requestId) {
        this(clientMessageCode, detailedMessage, requestId, null);
    }

    public final String getRequestId() {
        return requestId;
    }
}
