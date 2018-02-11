package com.jbtits.otus.lecture8;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class JsonSerializer {
    public static final String WRONG_CONTEXT_ERROR = "Context error";

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

    private EntryContext entryContext;
    private SourceContext sourceContext;

    private boolean isPrimitive(Class<?> fieldType) {
        return Arrays.stream(PRIMITIVES)
            .anyMatch(primitive -> primitive.isAssignableFrom(fieldType));
    }

    private EntryContext defineEntryContext(JSONAware entry) {
        if (entry == null) {
            return EntryContext.NULL;
        }
        Class<?> entryClass = entry.getClass();
        if (entryClass.equals(JSONArray.class)) {
            return EntryContext.ARRAY;
        } else if (entryClass.equals(JSONObject.class)) {
            return EntryContext.OBJECT;
        } else {
            return EntryContext.UNKNOWN;
        }
    }

    private SourceContext defineSourceContext(Object source) {
        if (source == null) {
            return SourceContext.NULL;
        }
        Class<?> sourceClass = source.getClass();
        if (isPrimitive(sourceClass)) {
            return SourceContext.PRIMITIVE;
        } else if (Iterable.class.isAssignableFrom(sourceClass)) {
            return SourceContext.ITERABLE;
        } else if (sourceClass.isArray()) {
            return SourceContext.ARRAY;
        } else if (Map.class.isAssignableFrom(sourceClass)) {
            return SourceContext.MAP;
        } else {
            return SourceContext.OBJECT;
        }
    }

    private void defineContext(JSONAware entry, Object source) {
        entryContext = defineEntryContext(entry);
        sourceContext = defineSourceContext(source);
        if (entryContext == EntryContext.UNKNOWN) {
            throw new RuntimeException(WRONG_CONTEXT_ERROR);
        }
    }

    private JSONAware attachJSONValue(JSONAware entry, Object value, String key) {
        if (entryContext == EntryContext.NULL) {
            entry = (JSONAware) value;
        } else if (entryContext == EntryContext.ARRAY) {
            ((JSONArray) entry).add(value);
        } else if (entryContext == EntryContext.OBJECT) {
            ((JSONObject) entry).put(key, value);
        }
        return entry;
    }

    private JSONAware attachJSONArray(JSONAware entry, Object source, String key) {
        JSONArray arrayEntry = new JSONArray();
        entry = attachJSONValue(entry, arrayEntry, key);
        if (sourceContext == SourceContext.ITERABLE) {
            for (Object e : (Iterable) source) {
                buildTree(arrayEntry, e,null);
            }
        } else if (sourceContext == SourceContext.ARRAY) {
            int length = Array.getLength(source);
            for (int i = 0; i < length; i ++) {
                Object element = Array.get(source, i);
                buildTree(arrayEntry, element, null);
            }
        }
        return entry;
    }

    private JSONAware attachJSONObject(JSONAware entry, Object source, String key) {
        JSONObject objectEntry = new JSONObject();
        entry = attachJSONValue(entry, objectEntry, key);
        if (sourceContext == SourceContext.MAP) {
            ((Map<String, Object>) source).forEach((key1, value) -> buildTree(objectEntry, value, key1));
        } else if (sourceContext == SourceContext.OBJECT) {
            Field fields[] = source.getClass().getDeclaredFields();
            for (Field field : fields) {
                Object value;
                field.setAccessible(true);
                try {
                    value = field.get(source);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                buildTree(objectEntry, value, field.getName());
            }
        }
        return entry;
    }

    private JSONAware buildTree(JSONAware entry, Object source, String key) {
        defineContext(entry, source);
        if (sourceContext == SourceContext.PRIMITIVE || sourceContext == SourceContext.NULL) {
            if (entryContext == EntryContext.NULL) {
                entry = new JSONPrimitive(source);
            } else {
                entry = attachJSONValue(entry, source, key);
            }
        } else if (sourceContext == SourceContext.ARRAY || sourceContext == SourceContext.ITERABLE) {
            entry = attachJSONArray(entry, source, key);
        } else if (sourceContext == SourceContext.MAP || sourceContext == SourceContext.OBJECT) {
            entry = attachJSONObject(entry, source, key);
        }
        return entry;
    }

    public String toJson(final Object source) {
        return buildTree(null, source, null).toJSONString();
    }

    private class JSONPrimitive implements JSONAware {
        Object source;

        public JSONPrimitive(Object source) {
            this.source = source;
        }

        @Override
        public String toJSONString() {
            if (source == null) {
                return "null";
            } else if (source.getClass().equals(String.class)) {
                return "\"" + source + "\"";
            }
            return source.toString();
        }
    }

    private enum EntryContext {
        UNKNOWN, NULL, OBJECT, ARRAY
    }

    private enum SourceContext {
        NULL, PRIMITIVE, ITERABLE, ARRAY, MAP, OBJECT
    }
}
