package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.commons.util.AnnotationUtils;

import java.lang.reflect.Method;

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
        return AnnotationUtils.isRemoteService(clazz);
    }

    @Override
    protected boolean validateMethodAnnotation(Method method) {
        return AnnotationUtils.isRemoteMethod(method);
    }
}
