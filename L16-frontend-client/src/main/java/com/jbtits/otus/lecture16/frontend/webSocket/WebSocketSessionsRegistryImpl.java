package com.jbtits.otus.lecture16.frontend.webSocket;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class WebSocketSessionsRegistryImpl implements WebSocketSessionsRegistry {
    private ConcurrentMap<String, WebSocketSession> sessions;
    private ConcurrentMap<String, Long> users;
    private ConcurrentMap<String, String> uuidsToSessionId;
    private static final int SEND_TIME_LIMIT = 1000;
    private static final int BUFFER_SIZE_LIMIT = 1024;

    public WebSocketSessionsRegistryImpl() {
        sessions = new ConcurrentHashMap<>();
        uuidsToSessionId = new ConcurrentHashMap<>();
        users = new ConcurrentHashMap<>();
    }

    @Override
    public void register(WebSocketSession session, String uuid) {
        ConcurrentWebSocketSessionDecorator concurrentSession
            = new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT, BUFFER_SIZE_LIMIT);
        sessions.put(session.getId(), concurrentSession);
        uuidsToSessionId.put(uuid, session.getId());
    }

    @Override
    public void unregister(String sessionId) {
        users.remove(sessionId);
        removeAllUuidLinks(sessionId);
        sessions.remove(sessionId);
    }

    private void removeAllUuidLinks(String sessionId) {
        Set<String> uuids = uuidsToSessionId.entrySet().stream().filter(e -> e.getValue().equals(sessionId))
            .map(e -> e.getKey()).collect(Collectors.toSet());
        uuidsToSessionId.keySet().removeAll(uuids);
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
    public String getSessionIdByUuid(String uuid) {
        return uuidsToSessionId.get(uuid);
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
