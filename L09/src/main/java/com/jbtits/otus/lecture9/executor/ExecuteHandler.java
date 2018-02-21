package com.jbtits.otus.lecture9.executor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ExecuteHandler {
    void accept(PreparedStatement statement) throws SQLException;
}
