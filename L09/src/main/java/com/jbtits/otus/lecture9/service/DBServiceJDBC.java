package com.jbtits.otus.lecture9.service;

import com.jbtits.otus.lecture9.db.ConnectionHelper;
import com.jbtits.otus.lecture9.entity.UserDataSet;
import com.jbtits.otus.lecture9.executor.Executor;

import java.sql.Connection;

public class DBServiceJDBC implements DBService {
    private final Connection connection;
    private Executor executor;
    private static final String CREATE_TABLE_USER = "create table if not exists user (id bigint(20) not null auto_increment, name varchar(255), age int(3))";

    public DBServiceJDBC() {
        connection = ConnectionHelper.getConnection();
        executor = new Executor(getConnection());
    }

    @Override
    public void prepareTables() {
        executor.execUpdate(CREATE_TABLE_USER);
    }

    @Override
    public void saveUser(UserDataSet user) {
        executor.save(user);
    }

    @Override
    public UserDataSet getUserById(long id) {
        return executor.load(id, UserDataSet.class);
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    protected Connection getConnection() {
        return connection;
    }
}
