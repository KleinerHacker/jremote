package org.pcsoft.framework.jremote.core.internal.type.wrapper;

/**
 * Abstract base class for all client wrappers
 */
public abstract class ClientWrapper {
    protected final Object clientProxy;

    public ClientWrapper(Object clientProxy) {
        this.clientProxy = clientProxy;
    }
}
