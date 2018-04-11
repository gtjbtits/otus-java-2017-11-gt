package com.jbtits.otus.lecture16.ms.utils.reflection;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class SimpleField {
    private String name;
    private Class<?> clazz;
    private Object value;
    private Annotation annotations[];

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation annotations[]) {
        this.annotations = annotations;
    }

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

    @Override
    public String toString() {
        return "SimpleField{" +
                "name='" + name + '\'' +
                ", clazz=" + clazz +
                ", value=" + value +
                ", annotations=" + Arrays.toString(annotations) +
                '}';
    }
}
