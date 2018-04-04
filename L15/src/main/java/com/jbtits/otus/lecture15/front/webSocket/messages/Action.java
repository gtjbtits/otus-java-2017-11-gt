package com.jbtits.otus.lecture15.front.webSocket.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Action {
    private long timestamp;
    private String action;

    public Action() {
        this.timestamp = System.currentTimeMillis();
    }

    public Action(String action) {
        this();
        this.action = action;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAction() {
        return action;
    }
}