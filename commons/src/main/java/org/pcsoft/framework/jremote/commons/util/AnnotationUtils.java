package org.pcsoft.framework.jremote.commons.util;

import org.pcsoft.framework.jremote.api.PushModelProperty;
import org.pcsoft.framework.jremote.api.RemotePushModel;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.internal.RemoteMethod;
import org.pcsoft.framework.jremote.api.internal.RemoteService;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class AnnotationUtils {
    public static boolean isRemoteService(Class<?> clazz) {
        return Arrays.stream(clazz.getAnnotations())
                .anyMatch(a -> a.annotationType().getAnnotation(RemoteService.class) != null);
    }

    public static boolean isRemoteMethod(Method method) {
        return Arrays.stream(method.getAnnotations())
                .anyMatch(a -> a.annotationType().getAnnotation(RemoteMethod.class) != null);
    }

    public static boolean isRemoteModel(Class<?> clazz) {
        return clazz.getAnnotation(RemotePushModel.class) != null;
    }

    public static boolean isModelProperty(Method method) {
        return method.getAnnotation(PushModelProperty.class) != null;
    }

    public static List<Method> getModelProperties(Class<?> clazz) {
        final List<Class<?>> classList = ReflectionUtils.findInterfaces(clazz, cl -> cl.getAnnotation(RemotePushModel.class) != null);
        if (classList.isEmpty())
            throw new JRemoteAnnotationException("Unable to find any remote model interface implement by the given class " + clazz.getName());

        return classList.stream()
                .map(cl -> Arrays.stream(cl.getDeclaredMethods())
                        .filter(m -> m.getAnnotation(PushModelProperty.class) != null)
                        .toArray(Method[]::new))
                .flatMap(Stream::of)
                .collect(Collectors.toList());
    }

    private AnnotationUtils() {
    }
}
