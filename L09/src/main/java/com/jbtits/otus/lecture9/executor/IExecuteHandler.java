package com.jbtits.otus.lecture9.executor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface IExecuteHandler {
    void accept(PreparedStatement statement) throws SQLException;
}
