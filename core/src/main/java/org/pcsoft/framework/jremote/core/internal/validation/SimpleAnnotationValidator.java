package org.pcsoft.framework.jremote.core.internal.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Abstract base for a simple variant of the service annotation validator
 */
abstract class SimpleAnnotationValidator extends AnnotationValidator {
    @Override
    protected boolean validateClassAnnotation(Class<?> clazz) {
        return clazz.getAnnotation(getRemoteServiceAnnotation()) != null;
    }

    @Override
    protected boolean validateMethodAnnotation(Method method) {
        return method.getAnnotation(getRemoteMethodAnnotation()) != null;
    }

    /**
     * Returns the default annotation for the service (class annotation)
     *
     * @return
     */
    protected abstract Class<? extends Annotation> getRemoteServiceAnnotation();

    /**
     * Returns the default annotation for a method (method annotation)
     *
     * @return
     */
    protected abstract Class<? extends Annotation> getRemoteMethodAnnotation();
}
