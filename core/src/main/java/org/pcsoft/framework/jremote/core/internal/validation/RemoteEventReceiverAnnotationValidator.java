package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.api.EventReceiverListener;
import org.pcsoft.framework.jremote.api.RemoteEventReceiver;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.EventReceivedListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

final class RemoteEventReceiverAnnotationValidator extends SimpleAnnotationValidator {
    private static final RemoteEventReceiverAnnotationValidator INSTANCE = new RemoteEventReceiverAnnotationValidator();

    public static RemoteEventReceiverAnnotationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected boolean validateMethodAnnotation(Method method) {
        if (!super.validateMethodAnnotation(method))
            return false;

        if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != EventReceivedListener.class || method.getReturnType() != void.class)
            throw new JRemoteAnnotationException(String.format("Method signature wrong: need a one-parameter method (%s) with a void return value: %s#%s",
                    EventReceivedListener.class.getName(), method.getDeclaringClass().getName(), method.getName()));

        return true;
    }

    @Override
    protected Class<? extends Annotation> getRemoteServiceAnnotation() {
        return RemoteEventReceiver.class;
    }

    @Override
    protected Class<? extends Annotation> getRemoteMethodAnnotation() {
        return EventReceiverListener.class;
    }

    @Override
    protected String getServiceName() {
        return "Remote Event Observer";
    }

    @Override
    protected String getServiceMethodName() {
        return "Event Observer Listener Method";
    }

    private RemoteEventReceiverAnnotationValidator() {
    }
}
