package com.jbtits.otus.lecture9.executor;

import com.jbtits.otus.lecture9.entity.DataSet;

import java.util.ArrayList;

public class StatementGenerator {
    private static final String INSERT_SKELETON = "insert into %s (%s) values (%s)";

    private <T extends DataSet> String getTableName(Class<T> clazz) {
        String className = clazz.getSimpleName();
        int dataSetLength = DataSet.class.getSimpleName().length();
        return className.substring(0, className.length() - dataSetLength).toLowerCase() + "s";
    }
    public <T extends DataSet> ParameterizedStatement generateInsert(T dataSet) {
        String table = getTableName(dataSet.getClass());
        String columns = "name, age";
        String params = "?, ?";
        return new ParameterizedStatement(String.format(INSERT_SKELETON, table, columns, params), new ArrayList<>());
    }
}
