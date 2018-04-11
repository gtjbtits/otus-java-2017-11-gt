package com.jbtits.otus.lecture16.frontend.webSocket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageAction extends Action {
    @JsonProperty(required = true)
    private String message;

    public MessageAction() {
        super();
    }

    public MessageAction(String uuid, String action, String message) {
        super(uuid, action);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
