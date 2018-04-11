package com.jbtits.otus.lecture16.ms.messages;

import com.jbtits.otus.lecture16.ms.app.Msg;

public class SignupResponseMsg extends Msg {
    private final boolean success;

    public SignupResponseMsg(String uuid, boolean success) {
        super(SignupResponseMsg.class, uuid);
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
