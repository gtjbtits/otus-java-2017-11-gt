package com.jbtits.otus.lecture9.executor;

import com.jbtits.otus.lecture9.db.ResultHandler;
import com.jbtits.otus.lecture9.entity.DataSet;
import com.jbtits.otus.lecture9.serializers.SQLSerializer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Executor {
    public Connection getConnection() {
        return connection;
    }

    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    private void execQuery(String query, PreparedExecutor.ExecuteHandler prepare, ResultHandler handler) {
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            prepare.accept(stmt);
            ResultSet result = stmt.getResultSet();
            handler.handle(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void execUpdate(String update, PreparedExecutor.ExecuteHandler prepare) {
        try (PreparedStatement stmt = getConnection().prepareStatement(update)) {
            prepare.accept(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T extends DataSet> void save(T user) {
        String preparedInsert = "insert into " + getTableName(user.getClass()) + " (name, age) values(?, ?)";
        execUpdate(preparedInsert, stmt -> {
            stmt.setString(1, "John");
            stmt.setInt(2, 10);
            System.out.println(stmt.toString());
            stmt.execute();
        });
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) {
        String preparedSelect = "select id, name, age from " + getTableName(clazz) + " where id = ?";
        execQuery(preparedSelect, stmt -> {
            stmt.setLong(1,1);
            System.out.println(stmt.toString());
            stmt.execute();
        }, result -> {
            List<String> names = new ArrayList<>();
            while (!result.isLast()) {
                result.next();
                names.add(result.getString("name"));
            }
            System.out.println(names);
        });
        return null;
    }

    private <T extends DataSet> String getTableName(Class<T> clazz) {
        String className = clazz.getSimpleName();
        int dataSetLength = DataSet.class.getSimpleName().length();
        return className.substring(0, className.length() - dataSetLength).toLowerCase() + "s";
    }
}
