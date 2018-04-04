package com.jbtits.otus.lecture15.front.webSocket;

public interface WebSocketSessionsRegistry {
    boolean register(String sessionId, Long userId);

    void unregister(String sessionId);

    Long getUserId(String sessionId);

    boolean isRegistered(String sessionId);
}
