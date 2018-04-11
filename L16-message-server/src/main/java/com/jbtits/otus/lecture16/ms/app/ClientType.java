package com.jbtits.otus.lecture16.ms.app;

public enum ClientType {
    DB_SERVICE("db"), FRONTEND_SERVICE("front");

    private final String address;

    ClientType(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return address;
    }
}
