package com.jbtits.otus.lecture16.frontend.webSocket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessAction extends Action {
    @JsonProperty(required = true)
    private boolean success;

    public SuccessAction() {
        super();
    }

    public SuccessAction(String uuid, String action, boolean success) {
        super(uuid, action);
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
