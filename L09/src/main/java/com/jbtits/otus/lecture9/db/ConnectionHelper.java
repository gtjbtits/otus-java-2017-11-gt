package com.jbtits.otus.lecture9.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {

    static private final String CONNECTION_STRING = "jdbc:h2:mem:test";

    static public Connection getConnection() {
        try {
            DriverManager.registerDriver(new org.h2.Driver());
            String url = CONNECTION_STRING;
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
