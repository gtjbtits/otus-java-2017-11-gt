package com.jbtits.otus.lecture16.ms.config;

public class MSServerConfiguration extends MSConfiguration {
    private final int port;

    public MSServerConfiguration() {
        super("server.properties");
        port = getInt("port");
    }

    public int getPort() {
        return port;
    }
}
