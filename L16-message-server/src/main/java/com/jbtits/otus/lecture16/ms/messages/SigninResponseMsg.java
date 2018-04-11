package com.jbtits.otus.lecture16.ms.messages;

import com.jbtits.otus.lecture16.ms.app.Msg;

public class SigninResponseMsg extends Msg {
    private final long userId;

    public SigninResponseMsg(String uuid, long userId) {
        super(SigninResponseMsg.class, uuid);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
