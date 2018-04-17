package com.jbtits.otus.lecture16.ms.app;

import java.util.UUID;

public class Address {
    private final ClientType type;

    public Address(ClientType type) {
        this.type = type;
    }

    public ClientType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Address{" +
                "type=" + type +
                '}';
    }
}
