package com.jbtits.otus.lecture16.ms.config;

public class MSClientConfiguration extends MSConfiguration {
    private final String serverHost;
    private final int serverPort;

    public MSClientConfiguration() {
        super("client.properties");
        serverHost = getString("serverHost");
        serverPort = getInt("serverPort");
    }

    public String getServerHost() {
        return serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }
}
