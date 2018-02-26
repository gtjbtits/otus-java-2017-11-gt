package com.jbtits.otus.lecture10.dbService;

import com.jbtits.otus.lecture10.jdbc.dao.UserDataSetDAO;
import com.jbtits.otus.lecture10.dataSets.UserDataSet;
import com.jbtits.otus.lecture10.db.ConnectionHelper;
import com.jbtits.otus.lecture10.jdbc.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class DBServiceJDBCImpl implements DBService {
    private final Connection connection;
    private Executor executor;
    private static final String CREATE_TABLE_USER = "create table if not exists user (id bigint(20) not null auto_increment, name varchar(255), age int(3))";

    public DBServiceJDBCImpl() {
        connection = ConnectionHelper.getConnection();
        executor = new Executor(getConnection());
        prepareTables();
    }

    private void prepareTables() {
        executor.execUpdate(CREATE_TABLE_USER);
    }

    @Override
    public void saveUser(UserDataSet user) {
        UserDataSetDAO dao = new UserDataSetDAO(getConnection());
        dao.save(user);
    }

    @Override
    public UserDataSet getUserById(long id) {
        UserDataSetDAO dao = new UserDataSetDAO(getConnection());
        return dao.load(id);
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
