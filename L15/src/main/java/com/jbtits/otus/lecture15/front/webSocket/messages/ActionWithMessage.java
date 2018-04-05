package com.jbtits.otus.lecture15.front.webSocket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionWithMessage extends Action {
    @JsonProperty(required = true)
    private String message;

    public ActionWithMessage() {
        super();
    }

    public ActionWithMessage(String action, String message) {
        super(action);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
