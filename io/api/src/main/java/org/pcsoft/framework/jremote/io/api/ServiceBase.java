package org.pcsoft.framework.jremote.io.api;

/**
 * Basic implementation for a service. Contains the basic implementation.
 */
public abstract class ServiceBase implements Service {
    private Object serviceImplementation;

    @Override
    public final Object getServiceImplementation() {
        return serviceImplementation;
    }

    @Override
    public final void setServiceImplementation(Object serviceImplementation) {
        this.serviceImplementation = serviceImplementation;
    }
}
