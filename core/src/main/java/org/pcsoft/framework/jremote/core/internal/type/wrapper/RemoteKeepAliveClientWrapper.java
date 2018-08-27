package org.pcsoft.framework.jremote.core.internal.type.wrapper;

import org.pcsoft.framework.jremote.np.api.annotation.KeepAlive;

public final class RemoteKeepAliveClientWrapper extends ClientWrapper {
    public RemoteKeepAliveClientWrapper(Object clientProxy) {
        super(clientProxy);
    }

    public boolean ping(String uuid) {
        return (boolean) findAssertAndInvokeMethod(
                method -> method.getAnnotation(KeepAlive.class) != null,
                method -> method.getParameterCount() == 1 && method.getParameterTypes()[0] == String.class &&
                        (method.getReturnType() == Boolean.class || method.getReturnType() == boolean.class),
                uuid
        );
    }
}
