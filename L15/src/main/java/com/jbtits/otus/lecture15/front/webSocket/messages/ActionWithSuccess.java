package com.jbtits.otus.lecture15.front.webSocket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionWithSuccess extends Action {
    @JsonProperty(required = true)
    private boolean success;

    public ActionWithSuccess() {
        super();
    }

    public ActionWithSuccess(String action, boolean success) {
        super(action);
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
