package org.pcsoft.framework.jremote.np.api;

/**
 * Basic implementation for a service. Contains the basic implementation.
 */
public abstract class ServiceBase implements Service {
    protected final Object serviceImplementation;

    public ServiceBase(Object serviceImplementation) {
        if (serviceImplementation == null)
            throw new IllegalArgumentException("serviceImplementation must be set");

        this.serviceImplementation = serviceImplementation;
    }
}
