package com.jbtits.otus.lecture15.front.webSocket;

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
