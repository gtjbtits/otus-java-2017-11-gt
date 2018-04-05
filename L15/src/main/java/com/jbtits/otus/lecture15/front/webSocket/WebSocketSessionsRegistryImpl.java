package com.jbtits.otus.lecture15.front.webSocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WebSocketSessionsRegistryImpl implements WebSocketSessionsRegistry {
    private ConcurrentMap<String, WebSocketSession> sessions;
    private ConcurrentMap<String, Long> users;

    public WebSocketSessionsRegistryImpl() {
        sessions = new ConcurrentHashMap<>();
        users = new ConcurrentHashMap<>();
    }

    @Override
    public void register(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    @Override
    public void unregister(String sessionId) {
        users.remove(sessionId);
        sessions.remove(sessionId);
    }

    @Override
    public void setUserSession(String sessionId, long userId) {
        if (users.putIfAbsent(sessionId, userId) != null) {
            throw new RuntimeException("Session already has a user");
        }
    }

    @Override
    public Long getUserId(String sessionId) {
        return users.get(sessionId);
    }

    @Override
    public boolean hasUser(String sessionId) {
        return getUserId(sessionId) != null;
    }
}
