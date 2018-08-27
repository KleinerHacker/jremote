package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.np.api.annotation.KeepAlive;
import org.pcsoft.framework.jremote.np.api.annotation.RemoteKeepAliveService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

final class RemoteKeepAliveServiceAnnotationValidator extends SimpleAnnotationValidator {
    private static final RemoteKeepAliveServiceAnnotationValidator INSTANCE = new RemoteKeepAliveServiceAnnotationValidator();

    public static RemoteKeepAliveServiceAnnotationValidator getInstance() {
        return INSTANCE;
    }

    private RemoteKeepAliveServiceAnnotationValidator() {
    }

    @Override
    protected Class<? extends Annotation> getRemoteServiceAnnotation() {
        return RemoteKeepAliveService.class;
    }

    @Override
    protected Class<? extends Annotation> getRemoteMethodAnnotation() {
        return KeepAlive.class;
    }

    @Override
    protected String getServiceName() {
        return "Keep Alive Service";
    }

    @Override
    protected String getServiceMethodName() {
        return "Keep Alive Method";
    }

    @Override
    protected boolean validateMethodAnnotation(Method method) {
        if (!super.validateMethodAnnotation(method))
            return false;

        final KeepAlive keepAlive = method.getAnnotation(KeepAlive.class);
        if (keepAlive != null) {
            if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != String.class ||
                    (method.getReturnType() != boolean.class && method.getReturnType() != Boolean.class))
                throw new JRemoteAnnotationException("[Keep Alive, Ping]: Wrong method signature: expected are one parameter: String (UUID), return type boolean");
        } else {
            assert false;
        }

        return true;
    }
}
