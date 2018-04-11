package com.jbtits.otus.lecture16.ms.messages;

import com.jbtits.otus.lecture16.ms.app.Msg;

public class SignupMsg extends Msg {
    private final String login;
    private final String password;

    public SignupMsg(String uuid, String login, String password) {
        super(SignupMsg.class, uuid);
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
