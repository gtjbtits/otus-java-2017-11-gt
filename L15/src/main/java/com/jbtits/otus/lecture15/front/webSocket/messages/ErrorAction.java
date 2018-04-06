package com.jbtits.otus.lecture15.front.webSocket.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.jbtits.otus.lecture15.front.webSocket.WebSocketMessageMapperImpl.ERROR_ACTION;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorAction extends MessageAction {
    @JsonProperty
    private String trace;

    public ErrorAction() {
        super();
    }

    public ErrorAction(String uuid, String message) {
        super(uuid, ERROR_ACTION, message);
    }

    public ErrorAction(String uuid, String message, String trace) {
        this(uuid, message);
        this.trace = trace;
    }

    public String getTrace() {
        return trace;
    }
}
