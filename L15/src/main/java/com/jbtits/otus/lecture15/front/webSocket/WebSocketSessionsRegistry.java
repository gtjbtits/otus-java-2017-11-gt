package com.jbtits.otus.lecture15.front.webSocket;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketSessionsRegistry {
    void register(WebSocketSession session);

    void unregister(String sessionId);

    void setUserSession(String sessionId, long userId);

    Long getUserId(String sessionId);

    WebSocketSession getSession(String sessionId);

    WebSocketSession[] getUserSessions();

    boolean hasUser(String sessionId);
}
