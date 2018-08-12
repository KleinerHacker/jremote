package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Abstract base for all service annotation validators
 */
abstract class AnnotationValidator {
    /**
     * Run validation
     * @param clazz
     * @throws JRemoteAnnotationException
     */
    final void validate(Class<?> clazz) throws JRemoteAnnotationException {
        if (!clazz.isInterface())
            throw new JRemoteAnnotationException(String.format("Unable to use a java class as %s: %s", getServiceName(), clazz.getName()));

        if (!validateClassAnnotation(clazz))
            throw new JRemoteAnnotationException(String.format("Unable to find %s annotation on service interface %s", getServiceName(), clazz.getName()));

        validateClassMethods(clazz);
    }

    private void validateClassMethods(Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(m -> {
                    if (!m.isDefault() && !validateMethodAnnotation(m))
                        throw new JRemoteAnnotationException(String.format("Method %s#%s is not default nor has %s annotation",
                                m.getDeclaringClass().getName(), m.getName(), getServiceMethodName()));
                });

        for (final Class<?> superClass : clazz.getInterfaces()) {
            validateClassMethods(superClass);
        }
    }

    /**
     * Returns the service name
     * @return
     */
    protected abstract String getServiceName();

    /**
     * Returns the service method name
     * @return
     */
    protected abstract String getServiceMethodName();

    /**
     * Run validation for class annotation(s)
     * @param clazz
     * @return
     */
    protected abstract boolean validateClassAnnotation(Class<?> clazz);

    /**
     * Run validation for method annotation(s)
     * @param method
     * @return
     */
    protected abstract boolean validateMethodAnnotation(Method method);
}
