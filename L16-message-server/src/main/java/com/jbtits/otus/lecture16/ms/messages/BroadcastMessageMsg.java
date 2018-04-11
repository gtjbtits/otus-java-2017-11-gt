package com.jbtits.otus.lecture16.ms.messages;

import com.jbtits.otus.lecture16.ms.app.Msg;

import java.util.Date;

public class BroadcastMessageMsg extends Msg {
    private final String text;

    private final String userName;
    private final Date created;

    public BroadcastMessageMsg(String uuid, String text, String userName, Date created) {
        super(BroadcastMessageMsg.class, uuid);
        this.text = text;
        this.userName = userName;
        this.created = created;
    }

    public String getText() {
        return text;
    }

    public String getUserName() {
        return userName;
    }

    public Date getCreated() {
        return created;
    }
}
