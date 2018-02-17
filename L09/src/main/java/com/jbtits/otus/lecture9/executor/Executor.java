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

    public <T extends DataSet> void save(T dataSet) {
        String preparedInsert = "insert into users (name, age) values(?, ?)";
        StatementGenerator stmtGenerator = new StatementGenerator();
        System.out.println(stmtGenerator.generateInsert(dataSet));
        execUpdate(preparedInsert, stmt -> {
            stmt.setString(1, "John");
            stmt.setInt(2, 10);
            System.out.println(stmt.toString());
            stmt.execute();
        });
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) {
        String preparedSelect = "select id, name, age from users where id = ?";
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
}
