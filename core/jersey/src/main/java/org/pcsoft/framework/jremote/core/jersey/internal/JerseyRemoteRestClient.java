package org.pcsoft.framework.jremote.core.jersey.internal;

import org.pcsoft.framework.jremote.api.interf.RemoteRestClient;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;

public class JerseyRemoteRestClient<T extends RemoteRestService> implements RemoteRestClient<T> {
    private final T serviceClient;

    public JerseyRemoteRestClient(T serviceClient) {
        this.serviceClient = serviceClient;
    }

    @Override
    public T get() {
        return serviceClient;
    }
}
