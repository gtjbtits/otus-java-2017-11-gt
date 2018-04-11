package com.jbtits.otus.lecture16.frontend.webSocket;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WebSocketSessionsRegistryImpl implements WebSocketSessionsRegistry {
    private ConcurrentMap<String, WebSocketSession> sessions;
    private ConcurrentMap<String, Long> users;
    private static final int SEND_TIME_LIMIT = 1000;
    private static final int BUFFER_SIZE_LIMIT = 1024;

    public WebSocketSessionsRegistryImpl() {
        sessions = new ConcurrentHashMap<>();
        users = new ConcurrentHashMap<>();
    }

    @Override
    public void register(WebSocketSession session) {
        ConcurrentWebSocketSessionDecorator concurrentSession
            = new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT, BUFFER_SIZE_LIMIT);
        sessions.put(session.getId(), concurrentSession);
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
    public WebSocketSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public WebSocketSession[] getUserSessions() {
        return users.keySet().stream().map(sessionId -> sessions.get(sessionId)).toArray(WebSocketSession[]::new);
    }

    @Override
    public boolean hasUser(String sessionId) {
        return getUserId(sessionId) != null;
    }
}
