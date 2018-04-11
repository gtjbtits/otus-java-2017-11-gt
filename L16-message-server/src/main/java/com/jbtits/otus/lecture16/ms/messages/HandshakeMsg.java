package com.jbtits.otus.lecture16.ms.messages;

import com.jbtits.otus.lecture16.ms.app.Msg;
import com.jbtits.otus.lecture16.ms.app.ClientType;

public class HandshakeMsg extends Msg {
    private final ClientType clientType;

    public HandshakeMsg(String uuid, ClientType clientType) {
        super(HandshakeMsg.class, uuid);
        this.clientType = clientType;
    }

    public ClientType getClientType() {
        return clientType;
    }
}
