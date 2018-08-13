package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.api.internal.RemoteMethod;
import org.pcsoft.framework.jremote.api.internal.RemoteService;

import java.lang.reflect.Method;
import java.util.Arrays;

abstract class RemoteServiceAnnotationValidatorBase extends AnnotationValidator {
    @Override
    protected String getServiceName() {
        return "Remote Service";
    }

    @Override
    protected String getServiceMethodName() {
        return "Remote Method";
    }

    @Override
    protected boolean validateClassAnnotation(Class<?> clazz) {
        return Arrays.stream(clazz.getAnnotations())
                .anyMatch(a -> a.annotationType().getAnnotation(RemoteService.class) != null);
    }

    @Override
    protected boolean validateMethodAnnotation(Method method) {
        return Arrays.stream(method.getAnnotations())
                .anyMatch(a -> a.annotationType().getAnnotation(RemoteMethod.class) != null);
    }
}
