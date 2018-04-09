package com.jbtits.otus.lecture15.messageContext.messages;

import com.jbtits.otus.lecture15.front.webSocket.WebSocketErrorHandler;
import com.jbtits.otus.lecture15.messageSystem.Address;
import com.jbtits.otus.lecture15.messageSystem.Message;

public abstract class SessionMessage extends Message {
    private final String uuid;
    private final String sessionId;
    private final WebSocketErrorHandler errorHandler;

    public SessionMessage(Address from, Address to, String uuid, final String sessionId, final WebSocketErrorHandler errorHandler) {
        super(from, to);
        this.uuid = uuid;
        this.sessionId = sessionId;
        this.errorHandler = errorHandler;
    }

    protected String getSessionId() {
        return sessionId;
    }

    protected WebSocketErrorHandler getErrorHandler() {
        return errorHandler;
    }

    protected String getUuid() {
        return uuid;
    }
}
