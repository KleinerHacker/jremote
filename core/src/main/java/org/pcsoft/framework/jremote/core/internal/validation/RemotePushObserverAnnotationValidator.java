package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.api.PushObserverListener;
import org.pcsoft.framework.jremote.api.RemotePushObserver;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.PushChangedListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

final class RemotePushObserverAnnotationValidator extends SimpleAnnotationValidator {
    private static final RemotePushObserverAnnotationValidator INSTANCE = new RemotePushObserverAnnotationValidator();

    public static RemotePushObserverAnnotationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected boolean validateMethodAnnotation(Method method) {
        if (!super.validateMethodAnnotation(method))
            return false;

        if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != PushChangedListener.class || method.getReturnType() != void.class)
            throw new JRemoteAnnotationException(String.format("Method signature wrong: need a one-parameter method (%s) with a void return value: %s#%s",
                    PushChangedListener.class.getName(), method.getDeclaringClass().getName(), method.getName()));

        return true;
    }

    @Override
    protected Class<? extends Annotation> getRemoteServiceAnnotation() {
        return RemotePushObserver.class;
    }

    @Override
    protected Class<? extends Annotation> getRemoteMethodAnnotation() {
        return PushObserverListener.class;
    }

    @Override
    protected String getServiceName() {
        return "Remote Push Observer";
    }

    @Override
    protected String getServiceMethodName() {
        return "Push Observer Listener Method";
    }

    private RemotePushObserverAnnotationValidator() {
    }
}
