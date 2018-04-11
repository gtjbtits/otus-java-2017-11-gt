package com.jbtits.otus.lecture16.ms.messages;

import com.jbtits.otus.lecture16.ms.app.Msg;

public class SigninMsg extends Msg {
    private final String login;
    private final String password;

    public SigninMsg(String uuid, String login, String password) {
        super(SigninMsg.class, uuid);
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
