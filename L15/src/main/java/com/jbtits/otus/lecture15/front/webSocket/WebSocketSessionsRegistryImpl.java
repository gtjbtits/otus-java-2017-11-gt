package com.jbtits.otus.lecture15.front.webSocket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WebSocketSessionsRegistryImpl implements WebSocketSessionsRegistry {
    private ConcurrentMap<String, Long> sessions;

    public WebSocketSessionsRegistryImpl() {
        sessions = new ConcurrentHashMap<>();
    }

    @Override
    public boolean register(String sessionId, Long userId) {
        return sessions.putIfAbsent(sessionId, userId) == null;
    }

    @Override
    public void unregister(String sessionId) {
        sessions.remove(sessionId);
    }

    @Override
    public Long getUserId(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public boolean isRegistered(String sessionId) {
        return getUserId(sessionId) != null;
    }
}
