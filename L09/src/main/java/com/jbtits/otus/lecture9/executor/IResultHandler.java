package com.jbtits.otus.lecture9.executor;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface IResultHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}
