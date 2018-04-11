package com.jbtits.otus.lecture16.frontend.webSocket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthAction extends Action {
    @JsonProperty(required = true)
    private String login;
    @JsonProperty(required = true)
    private String password;

    public AuthAction() {
        super();
    }

    public AuthAction(String uuid, String action, String login, String password) {
        super(uuid, action);
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
