package com.jbtits.otus.lecture9.service;

import com.jbtits.otus.lecture9.db.ConnectionHelper;
import com.jbtits.otus.lecture9.entity.DataSet;
import com.jbtits.otus.lecture9.entity.UserDataSet;
import com.jbtits.otus.lecture9.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class DBServiceJDBC implements DBService {
    private final Connection connection;
    private Executor executor;
    private static final String CREATE_TABLE_USER = "create table if not exists user (id bigint(20) not null auto_increment, name varchar(255), age int(3))";

    public DBServiceJDBC() {
        connection = ConnectionHelper.getConnection();
        executor = new Executor(getConnection());
        prepareTables();
    }

    private void prepareTables() {
        executor.execUpdate(CREATE_TABLE_USER);
    }

    @Override
    public <T extends DataSet> void save(T dataSet) {
        executor.save(dataSet);
    }

    @Override
    public <T extends DataSet> T getById(long id, Class<T> clazz) {
        return executor.load(id, clazz);
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected Connection getConnection() {
        return connection;
    }
}
