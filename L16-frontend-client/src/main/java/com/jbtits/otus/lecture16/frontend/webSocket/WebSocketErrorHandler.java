package com.jbtits.otus.lecture16.frontend.webSocket;

public interface WebSocketErrorHandler {
    void handleException(WebSocketError e, String sessionId, String uuid);
}
