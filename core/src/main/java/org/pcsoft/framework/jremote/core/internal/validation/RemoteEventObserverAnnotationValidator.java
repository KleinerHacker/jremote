package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.api.EventObserverListener;
import org.pcsoft.framework.jremote.api.RemoteEventObserver;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.EventChangeListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

final class RemoteEventObserverAnnotationValidator extends SimpleAnnotationValidator {
    private static final RemoteEventObserverAnnotationValidator INSTANCE = new RemoteEventObserverAnnotationValidator();

    public static RemoteEventObserverAnnotationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected boolean validateMethodAnnotation(Method method) {
        if (!super.validateMethodAnnotation(method))
            return false;

        if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != EventChangeListener.class || method.getReturnType() != void.class)
            throw new JRemoteAnnotationException(String.format("Method signature wrong: need a one-parameter method (%s) with a void return value: %s#%s",
                    EventChangeListener.class.getName(), method.getDeclaringClass().getName(), method.getName()));

        return true;
    }

    @Override
    protected Class<? extends Annotation> getRemoteServiceAnnotation() {
        return RemoteEventObserver.class;
    }

    @Override
    protected Class<? extends Annotation> getRemoteMethodAnnotation() {
        return EventObserverListener.class;
    }

    @Override
    protected String getServiceName() {
        return "Remote Event Observer";
    }

    @Override
    protected String getServiceMethodName() {
        return "Event Observer Listener Method";
    }

    private RemoteEventObserverAnnotationValidator() {
    }
}
