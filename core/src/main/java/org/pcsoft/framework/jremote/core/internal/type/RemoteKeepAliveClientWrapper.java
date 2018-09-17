package org.pcsoft.framework.jremote.core.internal.type;

import org.pcsoft.framework.jremote.ext.np.api.annotation.KeepAlive;

public final class RemoteKeepAliveClientWrapper extends ClientWrapper {
    public RemoteKeepAliveClientWrapper(Object clientProxy) {
        super(clientProxy);
    }

    public boolean ping(String uuid) {
        return (boolean) findAndInvokeMethod(
                method -> method.getAnnotation(KeepAlive.class) != null,
                method -> method.getParameterCount() == 1 && method.getParameterTypes()[0] == String.class &&
                        (method.getReturnType() == Boolean.class || method.getReturnType() == boolean.class),
                uuid
        );
    }
}
