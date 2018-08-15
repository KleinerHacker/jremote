package org.pcsoft.framework.jremote.commons;

import org.pcsoft.framework.jremote.api.internal.RemoteMethod;
import org.pcsoft.framework.jremote.api.internal.RemoteService;

import java.lang.reflect.Method;
import java.util.Arrays;

public final class AnnotationUtils {
    public static boolean isRemoteService(Class<?> clazz) {
        return Arrays.stream(clazz.getAnnotations())
                .anyMatch(a -> a.annotationType().getAnnotation(RemoteService.class) != null);
    }

    public static boolean isRemoteMethod(Method method) {
        return Arrays.stream(method.getAnnotations())
                .anyMatch(a -> a.annotationType().getAnnotation(RemoteMethod.class) != null);
    }

    private AnnotationUtils() {
    }
}
