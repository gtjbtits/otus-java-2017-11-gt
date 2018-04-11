package com.jbtits.otus.lecture16.frontend.webSocket;

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
