package com.jbtits.otus.lecture16.frontend.webSocket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DateMessage extends MessageAction {
    @JsonProperty(required = true)
    private long date;

    public DateMessage() {
        super();
    }

    public DateMessage(String uuid, String action, String message, long date) {
        super(uuid, action, message);
        this.date = date;
    }

    public long getDate() {
        return date;
    }
}
