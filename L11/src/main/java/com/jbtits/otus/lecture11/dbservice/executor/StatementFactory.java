package com.jbtits.otus.lecture11.dbservice.executor;

import com.jbtits.otus.lecture11.dbservice.entity.DataSet;
import com.jbtits.otus.lecture11.dbservice.utils.reflection.ReflectionHelper;
import com.jbtits.otus.lecture11.dbservice.utils.reflection.SimpleField;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StatementFactory {
    private static final String INSERT_SKELETON = "insert into %s (%s) values (%s)";
    private static final String SELECT_SKELETON = "select %s from %s where id = (?)";

    private <T extends DataSet> String getTableName(Class<T> clazz) {
        String className = clazz.getSimpleName();
        int dataSetLength = DataSet.class.getSimpleName().length();
        return className.substring(0, className.length() - dataSetLength).toLowerCase();
    }

    public <T extends DataSet> ParameterizedStatement generateInsert(T dataSet) {
        String table = getTableName(dataSet.getClass());
        List<String> columns = new LinkedList<>();
        List<Object> params = new LinkedList<>();
        List<SimpleField> fields = ReflectionHelper.getFields(dataSet);
        for (SimpleField field : fields) {
            if (field.getName().equals("id") && ((Number) field.getValue()).longValue() == 0) {
                continue;
            }
            columns.add(field.getName());
            params.add(field.getValue());
        }
        String paramPlaceholder = String.join(",", Collections.nCopies(columns.size(), "?"));

        return new ParameterizedStatement(
            String.format(INSERT_SKELETON, table, String.join(",", columns), paramPlaceholder),
            params);
    }

    public <T extends DataSet> ParameterizedStatement generateSelectById(long id, Class<T> dataSetClass) {
        String table = getTableName(dataSetClass);
        List<String> columns = new LinkedList<>();
        List<Object> params = new LinkedList<>();
        params.add(id);

        List<SimpleField> fields = ReflectionHelper.getFields(dataSetClass);
        for (SimpleField field : fields) {
            columns.add(field.getName());
        }

        return new ParameterizedStatement(
            String.format(SELECT_SKELETON, String.join(",", columns), table),
            params);
    }
}
