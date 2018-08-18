package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.api.Event;
import org.pcsoft.framework.jremote.api.RemoteEventService;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

final class RemoteEventServiceAnnotationValidator extends SimpleAnnotationValidator {
    private static final RemoteEventServiceAnnotationValidator INSTANCE = new RemoteEventServiceAnnotationValidator();

    public static RemoteEventServiceAnnotationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected String getServiceName() {
        return "Remote Event Service";
    }

    @Override
    protected String getServiceMethodName() {
        return "Remote Event Method";
    }

    @Override
    protected Class<? extends Annotation> getRemoteServiceAnnotation() {
        return RemoteEventService.class;
    }

    @Override
    protected Class<? extends Annotation> getRemoteMethodAnnotation() {
        return Event.class;
    }

    @Override
    protected boolean validateMethodAnnotation(Method method) {
        if (!super.validateMethodAnnotation(method))
            return false;

        final Event event = method.getAnnotation(Event.class);
        assert event != null;

        if (method.getParameterCount() != 1 || method.getReturnType() != void.class)
            throw new JRemoteAnnotationException(String.format("[Remote Push Service]: Method signature wrong: need a one-parameter method with a void return value: %s#%s",
                    method.getDeclaringClass().getName(), method.getName()));

        return true;
    }

    private RemoteEventServiceAnnotationValidator() {
    }
}
