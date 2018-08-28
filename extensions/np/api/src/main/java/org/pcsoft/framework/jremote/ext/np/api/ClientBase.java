package org.pcsoft.framework.jremote.ext.np.api;

public abstract class ClientBase implements Client {
    protected final Class<?> serviceClass;

    public ClientBase(Class<?> serviceClass) {
        if (serviceClass == null)
            throw new IllegalArgumentException("serviceClass must be set");

        this.serviceClass = serviceClass;
    }
}
