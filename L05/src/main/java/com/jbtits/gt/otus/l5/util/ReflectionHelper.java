package com.jbtits.gt.otus.l5.util;

import com.jbtits.gt.otus.l5.suite.annotations.After;
import com.jbtits.gt.otus.l5.suite.annotations.Test;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by tully.
 */
@SuppressWarnings("SameParameterValue")
public class ReflectionHelper {
    public static final Object[] EMPTY_ARRAY = {};

    private ReflectionHelper() {
    }

    public static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                //return type.newInstance(); //deprecated
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Object getFieldValue(Object object, String name) {
        Field field = null;
        boolean isAccessible = true;
        try {
            field = object.getClass().getDeclaredField(name); //getField() for public fields
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null && !isAccessible) {
                field.setAccessible(false);
            }
        }
        return null;
    }

    static void setFieldValue(Object object, String name, Object value) {
        Field field = null;
        boolean isAccessible = true;
        try {
            field = object.getClass().getDeclaredField(name); //getField() for public fields
            isAccessible = field.isAccessible();
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null && !isAccessible) {
                field.setAccessible(false);
            }
        }
    }

    public static Object callMethod(Object object, String name, Object... args) throws InvocationTargetException {
        Method method = null;
        try {
            method = object.getClass().getMethod(name, toClasses(args));
            return method.invoke(object, args);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException("Method call error", e);
        }
    }

    static private Class<?>[] toClasses(Object[] args) {
        List<Class<?>> classes = Arrays.stream(args)
                .map(Object::getClass)
                .collect(Collectors.toList());
        return classes.toArray(new Class<?>[classes.size()]);
    }

    static public Set<Class<?>> getAllPackageClassesWithMethodAnnotation(String packageName, Class<? extends Annotation> annotation) {
        final Set<Class<?>> classes = new HashSet<>();
        new FastClasspathScanner(packageName)
                .matchClassesWithMethodAnnotation(annotation, (aClass, executable) -> classes.add(aClass))
                .scan();
        return classes;
    }

    static public boolean hasAnnotation(Method method, Class<? extends Annotation> annotation) {
        Annotation[] annotations = method.getAnnotations();
        return Arrays.stream(annotations).anyMatch(a -> {
            return a.annotationType().equals(annotation);
        });
    }
}
