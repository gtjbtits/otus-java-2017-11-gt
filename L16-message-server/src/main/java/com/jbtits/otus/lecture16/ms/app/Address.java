package com.jbtits.otus.lecture16.ms.app;

import java.util.UUID;

public class Address {
    public ClientType getType() {
        return type;
    }

    private final ClientType type;

    public String getUuid() {
        return uuid;
    }

    private String uuid;

    public Address(ClientType type) {
        this.type = type;
    }

    public void generateUuid() {
        if (uuid != null) {
            return;
        }
        uuid = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Address{" +
                "type=" + type +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
