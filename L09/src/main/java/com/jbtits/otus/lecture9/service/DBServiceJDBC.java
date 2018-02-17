package com.jbtits.otus.lecture9.service;

import com.jbtits.otus.lecture9.db.ConnectionHelper;
import com.jbtits.otus.lecture9.entity.DataSet;
import com.jbtits.otus.lecture9.entity.UserDataSet;
import com.jbtits.otus.lecture9.executor.Executor;
import com.jbtits.otus.lecture9.executor.LogExecutor;
import com.jbtits.otus.lecture9.executor.PreparedExecutor;
import com.jbtits.otus.lecture9.executor.TExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBServiceJDBC implements DBService {
    private final Connection connection;
    private static final String INSERT_INTO_USER = "insert into user (name) values(?)";
    private static final String CREATE_TABLE_USER = "create table if not exists users (id bigint(20) not null auto_increment, name varchar(255), age int(3))";

    public DBServiceJDBC() {
        connection = ConnectionHelper.getConnection();
    }

    @Override
    public void prepareTables() throws SQLException {
        LogExecutor exec = new LogExecutor(getConnection());
        exec.execUpdate(CREATE_TABLE_USER);
        System.out.println("Table created");
    }

    @Override
    public void saveUser(UserDataSet user) throws SQLException {
        Executor exec = new Executor(getConnection());
        exec.save(user);
    }

    @Override
    public UserDataSet getUserById(long id) {
        Executor exec = new Executor(getConnection());
        return exec.load(id, UserDataSet.class);
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void close() throws Exception {
        connection.close();
        System.out.println("Connection closed. Bye!");
    }

    protected Connection getConnection() {
        return connection;
    }
}
