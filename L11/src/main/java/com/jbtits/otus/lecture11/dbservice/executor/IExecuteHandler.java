package com.jbtits.otus.lecture11.dbservice.executor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface IExecuteHandler {
    void accept(PreparedStatement statement) throws SQLException;
}
