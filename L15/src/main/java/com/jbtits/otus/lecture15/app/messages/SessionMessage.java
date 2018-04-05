package com.jbtits.otus.lecture15.app.messages;

import com.jbtits.otus.lecture15.messageSystem.Address;
import com.jbtits.otus.lecture15.messageSystem.Message;

public abstract class SessionMessage extends Message {
    private final String sessionId;

    public SessionMessage(Address from, Address to, final String sessionId) {
        super(from, to);
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }
}
