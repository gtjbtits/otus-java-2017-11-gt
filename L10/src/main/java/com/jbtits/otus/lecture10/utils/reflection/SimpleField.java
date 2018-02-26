package com.jbtits.otus.lecture10.utils.reflection;

import java.lang.annotation.Annotation;

public class SimpleField {
    private String name;
    private Class<?> clazz;
    private Object value;
    private Annotation[] annotations;

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

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public boolean hasAnnotation(Class<?> annotationClass) {
        if (annotations == null) {
            return false;
        }
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(annotationClass)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSqlRelationAnnotation() {
        return hasAnnotation(javax.persistence.OneToOne.class) || hasAnnotation(javax.persistence.OneToMany.class);
    }
}
