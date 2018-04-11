package com.jbtits.otus.lecture16.ms.messages;

import com.jbtits.otus.lecture16.ms.app.Address;
import com.jbtits.otus.lecture16.ms.app.Msg;

public class HandshakeResponseMsg extends Msg {
    public Address getAddress() {
        return address;
    }

    private final Address address;

    public HandshakeResponseMsg(String uuid, Address address) {
        super(HandshakeResponseMsg.class, uuid);
        this.address = address;
    }
}
