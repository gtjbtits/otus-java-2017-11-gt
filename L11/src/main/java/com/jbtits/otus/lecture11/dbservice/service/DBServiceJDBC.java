package com.jbtits.otus.lecture11.dbservice.service;

import com.jbtits.otus.lecture11.cache.CacheEngine;
import com.jbtits.otus.lecture11.cache.MyElement;
import com.jbtits.otus.lecture11.dbservice.db.ConnectionHelper;
import com.jbtits.otus.lecture11.dbservice.entity.DataSet;
import com.jbtits.otus.lecture11.dbservice.entity.UserDataSet;
import com.jbtits.otus.lecture11.dbservice.executor.Executor;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;

public class DBServiceJDBC implements DBService {
    private final Connection connection;
    private Executor executor;
    private CacheEngine<String, DataSet> cacheEngine;
    private static final String CREATE_TABLE_USER = "create table if not exists user (id bigint(20) not null auto_increment, name varchar(255), age int(3))";
    private static final String DROP_TABLE_USER = "drop table user if exists";

    public DBServiceJDBC() {
        connection = ConnectionHelper.getConnection();
        executor = new Executor(getConnection());
        prepareTables();
    }

    public DBServiceJDBC(CacheEngine cacheEngine) {
        this();
        this.cacheEngine = cacheEngine;
    }

    private String encodeCacheId(long id, Class<?> clazz) {
        return clazz.getName() + "_" + id;
    }

    private <T extends DataSet> String encodeCacheId(T dataSet) {
        return encodeCacheId(dataSet.getId(), dataSet.getClass());
    }

    private void cachePut(DataSet dataSet) {
        if (cacheEngine == null) {
            return;
        }
        cacheEngine.put(new MyElement<>(encodeCacheId(dataSet), dataSet));
    }

    private DataSet cacheGet(long id, Class<?> clazz) {
        if (cacheEngine == null) {
            return null;
        }
        MyElement<String, DataSet> element = cacheEngine.get(encodeCacheId(id, clazz));
        if (element == null) {
            return null;
        }
        return element.getValue();
    }

    private void prepareTables() {
        executor.execUpdate(DROP_TABLE_USER);
        executor.execUpdate(CREATE_TABLE_USER);
    }

    @Override
    public void saveUser(UserDataSet user) {
        long id = executor.save(user);
        user.setId(id);
        cachePut(user);
    }

    @Override
    public UserDataSet getUserById(long id) {
        UserDataSet user = (UserDataSet) cacheGet(id, UserDataSet.class);
        if (user == null) {
            user = executor.load(id, UserDataSet.class);
            cachePut(user);
        }
        return user;
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
