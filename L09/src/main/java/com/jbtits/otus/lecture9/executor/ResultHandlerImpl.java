package com.jbtits.otus.lecture9.executor;

import com.jbtits.otus.lecture9.entity.DataSet;
import com.jbtits.otus.lecture9.utils.ArrayUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultHandlerImpl {

    public <T extends DataSet> T getObject(ResultSet resultSet, Class<T> clazz) {
        T dataSet;
        try {
            dataSet = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Field fields[] = ArrayUtils.concat(
                clazz.getSuperclass().getDeclaredFields(),
                clazz.getDeclaredFields()
        );
        for (Field field : fields) {
            String columnName = field.getName();
            Class<?> fieldClass = field.getType();
            field.setAccessible(true);
            try {
                if (Integer.class.isAssignableFrom(fieldClass) || int.class.isAssignableFrom(fieldClass)) {
                    field.set(dataSet, resultSet.getInt(columnName));
                } else if (String.class.isAssignableFrom(fieldClass)) {
                    field.set(dataSet, resultSet.getString(columnName));
                } else if (Long.class.isAssignableFrom(fieldClass) || long.class.isAssignableFrom(fieldClass)) {
                    field.set(dataSet, resultSet.getLong(columnName));
                } else {
                    throw new RuntimeException("Unsupported field type");
                }
            } catch (SQLException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return dataSet;
    }
}
