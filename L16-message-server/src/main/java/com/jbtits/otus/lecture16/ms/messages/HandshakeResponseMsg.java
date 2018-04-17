package com.jbtits.otus.lecture16.ms.messages;

import com.jbtits.otus.lecture16.ms.app.Address;
import com.jbtits.otus.lecture16.ms.app.ClientAddress;
import com.jbtits.otus.lecture16.ms.app.Msg;

public class HandshakeResponseMsg extends Msg {
    public ClientAddress getAddress() {
        return address;
    }

    private final ClientAddress address;

    public HandshakeResponseMsg(String uuid, ClientAddress address) {
        super(HandshakeResponseMsg.class, uuid);
        this.address = address;
    }
}
