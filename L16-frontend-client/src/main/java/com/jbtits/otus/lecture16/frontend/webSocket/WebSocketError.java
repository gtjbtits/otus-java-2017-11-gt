package com.jbtits.otus.lecture16.frontend.webSocket;

import com.jbtits.otus.lecture16.ms.messages.error.ErrorCode;

public class WebSocketError extends RuntimeException {
    public WebSocketError(ErrorCode code, Throwable e) {
        super(code.toString(), e);
    }

    public WebSocketError(ErrorCode code) {
        super(code.toString());
    }

    public WebSocketError() {
        this(ErrorCode.UNKNOWN);
    }
}
