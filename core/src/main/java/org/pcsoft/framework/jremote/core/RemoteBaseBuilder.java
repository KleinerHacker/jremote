package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.api.factory.ClientFactory;
import org.pcsoft.framework.jremote.api.factory.ServerFactory;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;

import java.util.ArrayList;
import java.util.List;

public abstract class RemoteBaseBuilder<T extends RemoteBaseBuilder<T>> {
    protected final ServerFactory serverFactory;
    protected final ClientFactory clientFactory;
    protected final List<Class<? extends RemoteRestService>> classList = new ArrayList<>();

    protected RemoteBaseBuilder(ServerFactory serverFactory, ClientFactory clientFactory) {
        this.serverFactory = serverFactory;
        this.clientFactory = clientFactory;
    }

    public T withRestService(final Class<? extends RemoteRestService> clazz) {
        classList.add(clazz);
        return (T) this;
    }
}
