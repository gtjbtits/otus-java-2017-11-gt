package com.jbtits.otus.lecture9.executor;

import com.jbtits.otus.lecture9.entity.DataSet;
import com.jbtits.otus.lecture9.utils.ArrayUtils;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class StatementGenerator {
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

        Field fields[] = ArrayUtils.concat(
                dataSet.getClass().getSuperclass().getDeclaredFields(),
                dataSet.getClass().getDeclaredFields()
        );
        for (Field field : fields) {
            String name = field.getName();
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(dataSet);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (name.equals("id") && ((Number) value).longValue() == 0) {
                continue;
            }
            columns.add(name);
            params.add(value);
        }
        String paramPlaceholder = String.join(",", Collections.nCopies(columns.size(), "?"));

        return new ParameterizedStatement(
            String.format(INSERT_SKELETON, table, String.join(",", columns), paramPlaceholder),
            params);
    }

    public <T extends DataSet> ParameterizedStatement generateSelect(Class<T> dataSetClass) {
        String table = getTableName(dataSetClass);
        List<String> columns = new LinkedList<>();

        Field fields[] = ArrayUtils.concat(
            dataSetClass.getSuperclass().getDeclaredFields(),
            dataSetClass.getDeclaredFields()
        );
        for (Field field : fields) {
            columns.add(field.getName());
            field.setAccessible(true);
        }

        return new ParameterizedStatement(
                String.format(SELECT_SKELETON, String.join(",", columns), table),
                new LinkedList<>());
    }

    public void setParameters(PreparedStatement stmt, List<?> params) throws SQLException {
        int stmtParamPos = 0;
        for (Object param : params) {
            stmtParamPos++;
            if (param == null) {
                stmt.setObject(stmtParamPos, null);
                continue;
            }
            Class<?> clazz = param.getClass();
            if (Integer.class.isAssignableFrom(clazz)) {
                stmt.setInt(stmtParamPos, (Integer) param);
            } else if (String.class.isAssignableFrom(clazz)) {
                stmt.setString(stmtParamPos, (String) param);
            } else if (Long.class.isAssignableFrom(clazz)) {
                stmt.setLong(stmtParamPos, (Long) param);
            } else {
                throw new RuntimeException("Unsupported param type");
            }
        }
    }
}
