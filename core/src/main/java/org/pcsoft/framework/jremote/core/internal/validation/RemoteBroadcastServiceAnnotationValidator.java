package org.pcsoft.framework.jremote.core.internal.validation;

import java.lang.reflect.Method;

final class RemoteBroadcastServiceAnnotationValidator extends RemoteServiceAnnotationValidatorBase {
    private static final RemoteBroadcastServiceAnnotationValidator INSTANCE = new RemoteBroadcastServiceAnnotationValidator();

    static RemoteBroadcastServiceAnnotationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected String getServiceName() {
        return "Broadcast" + super.getServiceName();
    }

    @Override
    protected String getServiceMethodName() {
        return "Broadcast" + super.getServiceMethodName();
    }

    @Override
    protected boolean validateMethodAnnotation(Method method) {
        if (!super.validateMethodAnnotation(method))
            return false;

        return method.getReturnType() == void.class;
    }

    private RemoteBroadcastServiceAnnotationValidator() {
    }
}
