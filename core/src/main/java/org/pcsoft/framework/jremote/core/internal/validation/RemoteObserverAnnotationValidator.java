package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.api.PushObserverListener;
import org.pcsoft.framework.jremote.api.RemotePushObserver;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.PushChangeListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

final class RemoteObserverAnnotationValidator extends SimpleAnnotationValidator {
    private static final RemoteObserverAnnotationValidator INSTANCE = new RemoteObserverAnnotationValidator();

    public static RemoteObserverAnnotationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected boolean validateMethodAnnotation(Method method) {
        if (!super.validateMethodAnnotation(method))
            return false;

        if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != PushChangeListener.class || method.getReturnType() != void.class)
            throw new JRemoteAnnotationException(String.format("Method signature wrong: need a one-parameter method (%s) with a void return value: %s#%s",
                    PushChangeListener.class.getName(), method.getDeclaringClass().getName(), method.getName()));

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
        return "Remote Observer";
    }

    @Override
    protected String getServiceMethodName() {
        return "Observer Listener Method";
    }

    private RemoteObserverAnnotationValidator() {
    }
}
