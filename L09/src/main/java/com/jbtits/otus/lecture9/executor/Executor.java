package com.jbtits.otus.lecture9.executor;

import com.jbtits.otus.lecture9.entity.DataSet;

import java.sql.*;

public class Executor {
    public Connection getConnection() {
        return connection;
    }

    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    private <T extends DataSet> T execQuery(String query, ExecuteHandler prepare, TResultHandler<T> handler) {
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            prepare.accept(stmt);
            ResultSet result = stmt.getResultSet();
            return handler.handle(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void execUpdate(String update, ExecuteHandler prepare) {
        try (PreparedStatement stmt = getConnection().prepareStatement(update)) {
            if (prepare == null) {
                return;
            }
            prepare.accept(stmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int execUpdate(String update) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(update);
            return stmt.getUpdateCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends DataSet> void save(T dataSet) {
        StatementGenerator stmtGenerator = new StatementGenerator();
        ParameterizedStatement pStmt = stmtGenerator.generateInsert(dataSet);
        execUpdate(pStmt.getStmt(), stmt -> {
            stmtGenerator.setParameters(stmt, pStmt.getParams());
            System.out.println(stmt.toString());
            stmt.execute();
        });
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) {
        StatementGenerator stmtGenerator = new StatementGenerator();
        ParameterizedStatement pStmt = stmtGenerator.generateSelect(clazz);
        return execQuery(pStmt.getStmt(), stmt -> {
            stmt.setLong(1, id);
            System.out.println(stmt);
            stmt.execute();
        }, result -> {
            if (!result.next()) {
                return null;
            }
            if (!result.isLast()) {
                throw new RuntimeException("Too many results");
            }
            ResultHandlerImpl handler = new ResultHandlerImpl();
            return handler.getObject(result, clazz);
        });
    }
}
