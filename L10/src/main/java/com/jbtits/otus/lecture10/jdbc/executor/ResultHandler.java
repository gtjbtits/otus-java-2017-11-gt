package com.jbtits.otus.lecture10.jdbc.executor;

import com.jbtits.otus.lecture10.dataSets.DataSet;
import com.jbtits.otus.lecture10.utils.reflection.ReflectionHelper;
import com.jbtits.otus.lecture10.utils.reflection.SimpleField;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ResultHandler {

    public <T extends DataSet> T fillDataSet(ResultSet resultSet, Class<T> clazz) {
        T dataSet;
        try {
            dataSet = clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        List<SimpleField> fields = ReflectionHelper.getFields(dataSet);
        for (SimpleField field : fields) {
            if (field.hasSqlRelationAnnotation()) {
                // Relations stub
                continue;
            }
            try {
                if (Integer.class.isAssignableFrom(field.getType()) || int.class.isAssignableFrom(field.getType())) {
                    field.setValue(resultSet.getInt(field.getName()));
                } else if (String.class.isAssignableFrom(field.getType())) {
                    field.setValue(resultSet.getString(field.getName()));
                } else if (Long.class.isAssignableFrom(field.getType()) || long.class.isAssignableFrom(field.getType())) {
                    field.setValue(resultSet.getLong(field.getName()));
                } else {
                    throw new RuntimeException("Unsupported field type");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        ReflectionHelper.setFieldValues(dataSet, fields);
        return dataSet;
    }

    public <T extends DataSet> IResultHandler<T> getOne(Class<T> clazz) {
        return resultSet -> {
            if (!resultSet.next()) {
                return null;
            }
            if (!resultSet.isLast()) {
                throw new RuntimeException("Too many results");
            }
            return fillDataSet(resultSet, clazz);
        };
    }
}
