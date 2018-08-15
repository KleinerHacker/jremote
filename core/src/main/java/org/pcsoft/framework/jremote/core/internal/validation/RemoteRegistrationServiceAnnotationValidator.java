package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.sc.api.annotation.Registration;
import org.pcsoft.framework.jremote.sc.api.annotation.RemoteRegistrationService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

final class RemoteRegistrationServiceAnnotationValidator extends SimpleAnnotationValidator {
    private static final RemoteRegistrationServiceAnnotationValidator INSTANCE = new RemoteRegistrationServiceAnnotationValidator();

    public static RemoteRegistrationServiceAnnotationValidator getInstance() {
        return INSTANCE;
    }

    private RemoteRegistrationServiceAnnotationValidator() {
    }

    @Override
    protected Class<? extends Annotation> getRemoteServiceAnnotation() {
        return RemoteRegistrationService.class;
    }

    @Override
    protected Class<? extends Annotation> getRemoteMethodAnnotation() {
        return Registration.class;
    }

    @Override
    protected String getServiceName() {
        return "Registration Service";
    }

    @Override
    protected String getServiceMethodName() {
        return "Registration Method";
    }

    @Override
    protected boolean validateMethodAnnotation(Method method) {
        if (!super.validateMethodAnnotation(method))
            return false;

        final Registration registration = method.getAnnotation(Registration.class);
        if (registration != null) {
            switch (registration.value()) {
                case Register:
                    if (method.getParameterCount() != 3 || method.getParameterTypes()[0] != String.class || method.getParameterTypes()[1] != String.class ||
                            (method.getParameterTypes()[2] != int.class && method.getParameterTypes()[2] != Integer.class) || method.getReturnType() != void.class)
                        throw new JRemoteAnnotationException("[Registration, Register]: Wrong method signature: expected are three parameters: String (UUID), String (host), int (port), return void");
                    break;
                case Unregister:
                    if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != String.class || method.getReturnType() != void.class)
                        throw new JRemoteAnnotationException("[Registration, Unregister]: Wrong method signature: expected are one parameter: String (UUID), return void");
                    break;
                default:
                    throw new RuntimeException();
            }
        } else {
            assert false;
        }

        return true;
    }
}
