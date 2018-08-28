package org.pcsoft.framework.jremote.core.internal.type.wrapper;

import org.pcsoft.framework.jremote.ext.np.api.annotation.Registration;

public final class RemoteRegistrationClientWrapper extends ClientWrapper {
    public RemoteRegistrationClientWrapper(Object clientProxy) {
        super(clientProxy);
    }

    public void register(String uuid, String host, int port) {
        findAssertAndInvokeMethod(
                method -> method.getAnnotation(Registration.class) != null &&
                        method.getAnnotation(Registration.class).value() == Registration.RegistrationType.Register,
                method -> method.getParameterCount() == 3 && method.getParameterTypes()[0] == String.class && method.getParameterTypes()[1] == String.class &&
                        (method.getParameterTypes()[2] == Integer.class || method.getParameterTypes()[2] == int.class) && method.getReturnType() == void.class,
                uuid, host, port
        );
    }

    public void unregister(String uuid) {
        findAssertAndInvokeMethod(
                method -> method.getAnnotation(Registration.class) != null &&
                        method.getAnnotation(Registration.class).value() == Registration.RegistrationType.Unregister,
                method -> method.getParameterCount() == 1 && method.getParameterTypes()[0] == String.class && method.getReturnType() == void.class,
                uuid
        );
    }
}
