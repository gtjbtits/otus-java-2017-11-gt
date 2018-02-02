package com.jbtits.otus.lecture8;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class JsonSerializer {
    private static final Class<?> PRIMITIVES[] = {
        Boolean.class,
        String.class,
        Byte.class,
        Short.class,
        Integer.class,
        Long.class,
        Float.class,
        Double.class
    };

    private boolean isPrimitive(Class<?> fieldType) {
        return Arrays.stream(PRIMITIVES)
            .anyMatch(primitive -> primitive.isAssignableFrom(fieldType));
    }

    private void pushJsonValue(Object key, Object value, JSONAware entry) {
        Class<?> entryClass = entry.getClass();
        if (entryClass.equals(JSONArray.class)) {
            ((JSONArray) entry).add(value);
        } else if (entryClass.equals(JSONObject.class)) {
            if (key == null) {
                throw new RuntimeException("Json object field name is null");
            }
            ((JSONObject) entry).put(key, value);
        } else {
            throw new RuntimeException("Unknown json entry");
        }
    }

    private JSONAware buildTree(final Object source, JSONAware entry, String key) {
        if (source == null) {
            pushJsonValue(key, null, entry);
            return entry;
        }
        Class<?> sourceClass = source.getClass();
        if (isPrimitive(sourceClass)) {
            if (entry == null) {
                throw new RuntimeException("Wrong JSON structure");
            }
            pushJsonValue(key, source, entry);
        } else if (Iterable.class.isAssignableFrom(sourceClass)) {
            JSONArray arrayEntry = new JSONArray();
            if (entry == null) {
                entry = arrayEntry;
            } else {
                pushJsonValue(key, arrayEntry, entry);
            }
            for (Object e : (Iterable) source) {
                buildTree(e, arrayEntry, null);
            }
        } else if (sourceClass.isArray()) {
            JSONArray arrayEntry = new JSONArray();
            if (entry == null) {
                entry = arrayEntry;
            } else {
                pushJsonValue(key, arrayEntry, entry);
            }
            int length = Array.getLength(source);
            for (int i = 0; i < length; i ++) {
                Object element = Array.get(source, i);
                buildTree(element, arrayEntry, null);
            }
        } else if (Map.class.isAssignableFrom(sourceClass)) {
            JSONObject objectEntry = new JSONObject();
            if (entry == null) {
                entry = objectEntry;
            } else {
                pushJsonValue(key, objectEntry, entry);
            }
            ((Map<String, Object>) source).forEach((key1, value) -> buildTree(value, objectEntry, key1));
        } else {
            JSONObject objectEntry = new JSONObject();
            if (entry == null) {
                entry = objectEntry;
            } else {
                pushJsonValue(key, objectEntry, entry);
            }
            Field fields[] = source.getClass().getDeclaredFields();
            for (Field field : fields) {
                Object value;
                field.setAccessible(true);
                try {
                    value = field.get(source);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                buildTree(value, objectEntry, field.getName());
            }
        }
        return entry;
    }

    public String toJson(final Object source) {
        return buildTree(source, null, null).toJSONString();
    }
}
