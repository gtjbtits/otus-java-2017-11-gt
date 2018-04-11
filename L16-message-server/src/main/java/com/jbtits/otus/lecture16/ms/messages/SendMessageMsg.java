package com.jbtits.otus.lecture16.ms.messages;

import com.jbtits.otus.lecture16.ms.app.Msg;

public class SendMessageMsg extends Msg {
    private final String text;
    private final long userId;

    public SendMessageMsg(String uuid, String text, long userId) {
        super(SendMessageMsg.class, uuid);
        this.text = text;
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public long getUserId() {
        return userId;
    }
}
