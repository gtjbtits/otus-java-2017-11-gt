package com.jbtits.otus.lecture16.frontend.webSocket;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketSessionsRegistry {
    void register(WebSocketSession session, String uuid);

    void unregister(String sessionId);

    void setUserSession(String sessionId, long userId);

    Long getUserId(String sessionId);

    WebSocketSession getSession(String sessionId);

    String getSessionIdByUuid(String uuid);

    WebSocketSession[] getUserSessions();

    boolean hasUser(String sessionId);
}
