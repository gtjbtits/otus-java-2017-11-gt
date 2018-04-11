package com.jbtits.otus.lecture16.ms.utils.reflection;

import com.jbtits.otus.lecture16.ms.utils.ArrayUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class ReflectionUtils {
    static public List<SimpleField> getFields(Object source, Class<?> stopClass) {
        if (source == null) {
            return null;
        }
        List<SimpleField> simpleFields = new LinkedList<>();
        Class<?> clazz = source.getClass();
        Field fields[] = collectFields(clazz, stopClass);
        for (Field field : fields) {
            SimpleField simple = new SimpleField();
            simple.setName(field.getName());
            simple.setType(field.getType());
            simple.setAnnotations(field.getAnnotations());
            field.setAccessible(true);
            try {
                simple.setValue(field.get(source));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            simpleFields.add(simple);
        }
        return simpleFields;
    }

    static private Field[] collectFields(Class<?> current, Class<?> stop) {
        Field fields[] = {};
        return collectFields(current, stop, fields);
    }

    static private Field[] collectFields(Class<?> current, Class<?> stop, Field[] fields) {
        if (stop.isAssignableFrom(current)) {
            fields = ArrayUtils.concat(fields, current.getDeclaredFields());
        } else {
            return fields;
        }
        if (current.getSuperclass() == null) {
            return fields;
        }
        return collectFields(current.getSuperclass(), stop, fields);
    }
}
