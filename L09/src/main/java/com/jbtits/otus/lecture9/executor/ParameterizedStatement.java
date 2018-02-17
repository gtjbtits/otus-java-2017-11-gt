package com.jbtits.otus.lecture9.executor;

import java.util.List;

public class ParameterizedStatement {
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
}
