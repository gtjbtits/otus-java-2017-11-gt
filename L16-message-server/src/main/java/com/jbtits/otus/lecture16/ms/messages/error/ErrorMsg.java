package com.jbtits.otus.lecture16.ms.messages.error;

import com.jbtits.otus.lecture16.ms.app.Msg;

public class ErrorMsg extends Msg {
    private Throwable cause;

    public ErrorMsg(String uuid, Throwable cause) {
        super(ErrorMsg.class, uuid);
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }
}
