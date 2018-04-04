package com.jbtits.otus.lecture15.front.webSocket;

public class WebSocketException extends Exception {
    public WebSocketException(String message) {
        super(message);
    }

    public WebSocketException(String message, Throwable cause) {
        super(message, cause);
    }
}
