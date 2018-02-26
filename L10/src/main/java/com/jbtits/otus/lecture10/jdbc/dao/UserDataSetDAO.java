package com.jbtits.otus.lecture10.jdbc.dao;

import com.jbtits.otus.lecture10.dataSets.UserDataSet;
import com.jbtits.otus.lecture10.jdbc.executor.Executor;

import java.sql.Connection;

public class UserDataSetDAO {
    private Executor executor;

    public UserDataSetDAO(Connection connection) {
        executor = new Executor(connection);
    }

    public void save(UserDataSet user) {
        executor.save(user);
    }

    public UserDataSet load(long id) {
        return executor.load(id, UserDataSet.class);
    }
}
