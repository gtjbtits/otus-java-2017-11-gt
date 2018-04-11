package com.jbtits.otus.lecture16.frontend.webSocket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BroadcastMessage extends DateMessage {
    @JsonProperty(required = true)
    private String name;

    public BroadcastMessage() {
        super();
    }

    public BroadcastMessage(String uuid, String action, String message, long date, String name) {
        super(uuid, action, message, date);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
