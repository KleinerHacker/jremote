package org.pcsoft.framework.jremote.io.api;

public abstract class ServiceBase implements Service {
    private Object serviceImplementation;

    @Override
    public Object getServiceImplementation() {
        return serviceImplementation;
    }

    @Override
    public void setServiceImplementation(Object serviceImplementation) {
        this.serviceImplementation = serviceImplementation;
    }
}
