package com.jbtits.otus.lecture11.dbservice.executor;

import com.jbtits.otus.lecture11.dbservice.entity.DataSet;

import java.sql.*;

public class Executor {
    protected Connection getConnection() {
        return connection;
    }

    private final Connection connection;
    private StatementFactory statementFactory;
    private ResultHandler resultHandler;

    public Executor(Connection connection) {
        this.connection = connection;
        statementFactory = new StatementFactory();
        resultHandler = new ResultHandler();
    }

    private <T extends DataSet> T execQuery(String query, IExecuteHandler prepare, IResultHandler<T> handler) {
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            prepare.accept(stmt);
            stmt.execute();
            ResultSet result = stmt.getResultSet();
            return handler.handle(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends DataSet> T execQuery(ParameterizedStatement parameterized, IResultHandler<T> handler) {
        return execQuery(parameterized.getStmt(), parameterized.setParams(), handler);
    }

    public void execUpdate(String update, IExecuteHandler prepare) {
        try (PreparedStatement stmt = getConnection().prepareStatement(update)) {
            prepare.accept(stmt);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long execInsert(String insert, IExecuteHandler prepare) {
        try (PreparedStatement stmt = getConnection().prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)) {
            prepare.accept(stmt);
            stmt.execute();
            ResultSet result = stmt.getGeneratedKeys();
            if (result.next()) {
                return result.getLong(1);
            } else {
                throw new SQLException("No generated keys are returned");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int execUpdate(String update) {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(update);
            return stmt.getUpdateCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void execUpdate(ParameterizedStatement parametrized) {
        execUpdate(parametrized.getStmt(), parametrized.setParams());
    }

    public long execInsert(ParameterizedStatement parameterized) {
        return execInsert(parameterized.getStmt(), parameterized.setParams());
    }

    public <T extends DataSet> long save(T dataSet) {
        return execInsert(statementFactory.generateInsert(dataSet));
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) {
        return execQuery(statementFactory.generateSelectById(id, clazz), resultHandler.getOne(clazz));
    }
}
