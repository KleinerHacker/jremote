package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.api.PushModelProperty;
import org.pcsoft.framework.jremote.api.RemotePushModel;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

final class RemotePushModelAnnotationValidator extends SimpleAnnotationValidator {
    private static final RemotePushModelAnnotationValidator INSTANCE = new RemotePushModelAnnotationValidator();

    public static RemotePushModelAnnotationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected boolean validateMethodAnnotation(Method method) {
        if (!super.validateMethodAnnotation(method))
            return false;

        if (method.getParameterCount() > 0 || method.getReturnType() == void.class)
            throw new JRemoteAnnotationException(String.format("[Remote Model]: Method signature wrong: need a parameter-less method with a concrete return value: %s#%s",
                    method.getDeclaringClass().getName(), method.getName()));

        return true;
    }

    @Override
    protected Class<? extends Annotation> getRemoteServiceAnnotation() {
        return RemotePushModel.class;
    }

    @Override
    protected Class<? extends Annotation> getRemoteMethodAnnotation() {
        return PushModelProperty.class;
    }

    @Override
    protected String getServiceName() {
        return "Remote Push Model";
    }

    @Override
    protected String getServiceMethodName() {
        return "Push Model Property Method";
    }

    private RemotePushModelAnnotationValidator() {
    }
}
