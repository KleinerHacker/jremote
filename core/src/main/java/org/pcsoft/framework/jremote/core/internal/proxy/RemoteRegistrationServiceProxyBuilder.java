package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.core.internal.registry.ClientRegistry;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;
import org.pcsoft.framework.jremote.ext.np.api.annotation.Registration;

import java.lang.reflect.Method;

final class RemoteRegistrationServiceProxyBuilder extends ProxyBuilder<Registration, ClientRegistry> {
    private static final RemoteRegistrationServiceProxyBuilder INSTANCE = new RemoteRegistrationServiceProxyBuilder();

    public static RemoteRegistrationServiceProxyBuilder getInstance() {
        return INSTANCE;
    }

    private RemoteRegistrationServiceProxyBuilder() {
        super(Registration.class);
    }

    @Override
    protected void validate(Class<?> clazz) throws JRemoteAnnotationException {
        Validator.validateForRemoteService(clazz);
        Validator.validateForRemoteRegistrationService(clazz);
    }

    @Override
    protected Object invokeMethod(Registration registration, ClientRegistry clientRegistry, Class<?> clazz, Method method, Object[] args) {
        switch (registration.value()) {
            case Register:
                clientRegistry.registerClient((String) args[0], (String) args[1], (int) args[2]);
                break;
            case Unregister:
                clientRegistry.unregisterClient((String) args[0]);
                break;
        }

        return null;
    }

    @Override
    protected String getProxyName() {
        return "Registration Service";
    }
}
