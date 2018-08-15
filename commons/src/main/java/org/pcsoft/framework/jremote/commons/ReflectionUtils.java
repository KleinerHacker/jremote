package org.pcsoft.framework.jremote.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class ReflectionUtils {
    public static List<Class<?>> findInterfaces(Class<?> clazz) {
        return findInterfaces(clazz, cl -> true);
    }

    public static List<Class<?>> findInterfaces(Class<?> clazz, Predicate<Class<?>> validator) {
        final List<Class<?>> list = new ArrayList<>();
        findNextInterfaces(clazz, list, validator);

        return list;
    }

    private static void findNextInterfaces(Class<?> clazz, List<Class<?>> list, Predicate<Class<?>> validator) {
        for (final Class<?> interfaceClass : clazz.getInterfaces()) {
            if (validator.test(interfaceClass)) {
                list.add(interfaceClass);
            }
            findNextInterfaces(interfaceClass, list, validator);
        }

        if (clazz.getSuperclass() != null) {
            findNextInterfaces(clazz.getSuperclass(), list, validator);
        }
    }

    private ReflectionUtils() {
    }
}
