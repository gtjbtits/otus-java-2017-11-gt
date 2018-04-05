package com.jbtits.otus.lecture15.front.webSocket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionWithAuth extends Action {
    @JsonProperty(required = true)
    private String login;
    @JsonProperty(required = true)
    private String password;

    public ActionWithAuth() {
        super();
    }

    public ActionWithAuth(String action, String login, String password) {
        super(action);
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
