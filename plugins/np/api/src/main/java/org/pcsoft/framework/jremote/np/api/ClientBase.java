package org.pcsoft.framework.jremote.np.api;

public abstract class ClientBase implements Client {
    private Class<?> serviceClass;

    @Override
    public final Class<?> getServiceClass() {
        return serviceClass;
    }

    @Override
    public final void setServiceClass(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
    }
}
