package com.jbtits.otus.lecture16.ms.app;

import java.util.UUID;

public class ClientAddress extends Address {
    private final String uuid;

    public ClientAddress(ClientType type, String uuid) {
        super(type);
        this.uuid = uuid;
    }

    public ClientAddress(ClientType type) {
        this(type, UUID.randomUUID().toString());
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "ClientAddress{" +
                "type='" + getType() + "\' ," +
                "uuid='" + uuid + '\'' +
                '}';
    }
}
