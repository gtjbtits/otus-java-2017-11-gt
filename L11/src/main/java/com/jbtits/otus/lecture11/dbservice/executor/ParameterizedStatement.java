package com.jbtits.otus.lecture11.dbservice.executor;

import java.util.List;

public class ParameterizedStatement {
    public String getStmt() {
        return stmt;
    }

    private String stmt;
    private List<?> params;

    @Override
    public String toString() {
        return "ParameterizedStatement{" +
                "stmt='" + stmt + '\'' +
                ", params=" + params +
                '}';
    }

    public ParameterizedStatement(String stmt, List<?> params) {
        this.stmt = stmt;
        this.params = params;
    }

    public IExecuteHandler setParams() {
        return prepared -> {
            int stmtParamPos = 0;
            for (Object param : params) {
                stmtParamPos++;
                if (param == null) {
                    prepared.setObject(stmtParamPos, null);
                    continue;
                }
                Class<?> clazz = param.getClass();
                if (Integer.class.isAssignableFrom(clazz)) {
                    prepared.setInt(stmtParamPos, (Integer) param);
                } else if (String.class.isAssignableFrom(clazz)) {
                    prepared.setString(stmtParamPos, (String) param);
                } else if (Long.class.isAssignableFrom(clazz)) {
                    prepared.setLong(stmtParamPos, (Long) param);
                } else {
                    throw new RuntimeException("Unsupported param type");
                }
            }
        };
    }
}
