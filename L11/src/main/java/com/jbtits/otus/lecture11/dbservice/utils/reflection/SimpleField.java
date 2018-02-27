package com.jbtits.otus.lecture11.dbservice.utils.reflection;

public class SimpleField {
    private String name;
    private Class<?> clazz;
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return clazz;
    }

    public void setType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
