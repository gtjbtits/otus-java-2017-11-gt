package com.jbtits.otus.lecture15.front.webSocket.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Action {
    @JsonProperty(required = true)
    private String uuid;
    @JsonProperty(value = "ts", required = true)
    private long timestamp;
    @JsonProperty(required = true)
    private String action;

    public Action() {
        this.timestamp = System.currentTimeMillis();
//        this.uuid = UUID.randomUUID().toString();
    }

    public Action(String uuid, String action) {
        this();
        this.uuid = uuid;
        this.action = action;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAction() {
        return action;
    }

    public String getUuid() {
        return uuid;
    }
}