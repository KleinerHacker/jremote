package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.api.PushMethod;
import org.pcsoft.framework.jremote.api.RemotePushService;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

final class RemotePushServiceAnnotationValidator extends SimpleAnnotationValidator {
    private static final RemotePushServiceAnnotationValidator INSTANCE = new RemotePushServiceAnnotationValidator();

    public static RemotePushServiceAnnotationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected String getServiceName() {
        return "Remote Push Service";
    }

    @Override
    protected String getServiceMethodName() {
        return "Remote Push Method";
    }

    @Override
    protected Class<? extends Annotation> getRemoteServiceAnnotation() {
        return RemotePushService.class;
    }

    @Override
    protected Class<? extends Annotation> getRemoteMethodAnnotation() {
        return PushMethod.class;
    }

    @Override
    protected boolean validateMethodAnnotation(Method method) {
        if (!super.validateMethodAnnotation(method))
            return false;

        final PushMethod pushMethod = method.getAnnotation(PushMethod.class);
        assert pushMethod != null;

        switch (pushMethod.type()) {
            case Simple:
            case CompleteList:
                if (method.getParameterCount() != 1 || method.getReturnType() != void.class)
                    throw new JRemoteAnnotationException(String.format("[Remote Push Service]: Method signature wrong: need a one-parameter method with a void return value: %s#%s",
                            method.getDeclaringClass().getName(), method.getName()));
                break;
            case SingleListItem:
                if (method.getParameterCount() != 2 || method.getReturnType() != void.class)
                    throw new JRemoteAnnotationException(String.format("[Remote Push Service]: Method signature wrong: need a two-parameter method with a void return value: %s#%s",
                            method.getDeclaringClass().getName(), method.getName()));
                break;
            default:
                throw new RuntimeException();
        }

        return true;
    }

    private RemotePushServiceAnnotationValidator() {
    }
}