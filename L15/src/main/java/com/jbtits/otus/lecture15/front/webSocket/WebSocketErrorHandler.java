package com.jbtits.otus.lecture15.front.webSocket;

public interface WebSocketErrorHandler {
    void handleException(WebSocketError e, String sessionId, String uuid);
}
