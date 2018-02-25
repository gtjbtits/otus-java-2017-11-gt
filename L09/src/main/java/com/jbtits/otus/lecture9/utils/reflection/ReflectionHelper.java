package com.jbtits.otus.lecture9.utils.reflection;

import com.jbtits.otus.lecture9.utils.ArrayUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class ReflectionHelper {
    static public List<SimpleField> getFields(Object source) {
        if (source == null) {
            return null;
        }
        List<SimpleField> simpleFields = new LinkedList<>();
        Class<?> clazz = source.getClass();
        Field fields[] = ArrayUtils.concat(
            clazz.getSuperclass().getDeclaredFields(),
            clazz.getDeclaredFields()
        );
        for (Field field : fields) {
            SimpleField simple = new SimpleField();
            simple.setName(field.getName());
            simple.setType(field.getType());
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

    static public List<SimpleField> getFields(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        List<SimpleField> simpleFields = new LinkedList<>();
        Field fields[] = ArrayUtils.concat(
            clazz.getSuperclass().getDeclaredFields(),
            clazz.getDeclaredFields()
        );
        for (Field field : fields) {
            SimpleField simple = new SimpleField();
            simple.setName(field.getName());
            simple.setType(field.getType());
            simpleFields.add(simple);
        }
        return simpleFields;
    }

    static public void setFieldValues(Object target, List<SimpleField> simpleFields) {
        if (target == null || simpleFields == null) {
            return;
        }
        Class<?> clazz = target.getClass();
        for (SimpleField simple : simpleFields) {
            try {
                Field field;
                try {
                    field = clazz.getDeclaredField(simple.getName());
                } catch (NoSuchFieldException e) {
                    field = clazz.getSuperclass().getDeclaredField(simple.getName());
                }
                field.setAccessible(true);
                field.set(target, simple.getValue());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
