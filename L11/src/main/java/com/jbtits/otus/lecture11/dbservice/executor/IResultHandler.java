package com.jbtits.otus.lecture11.dbservice.executor;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface IResultHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}
