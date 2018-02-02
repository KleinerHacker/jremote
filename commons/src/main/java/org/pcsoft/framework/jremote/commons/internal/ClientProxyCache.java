package org.pcsoft.framework.jremote.commons.internal;

import org.pcsoft.framework.jremote.api.interf.RemoteRestClient;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;
import org.pcsoft.framework.jremote.api.internal.ClientProxyFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class ClientProxyCache {
    private final ClientProxyFactory clientProxyFactory;
    private final Map<Class<? extends RemoteRestService>, RemoteRestClient<? extends RemoteRestService>> cacheMap = new HashMap<>();

    public ClientProxyCache(ClientProxyFactory clientProxyFactory) {
        this.clientProxyFactory = clientProxyFactory;
    }

    public RemoteRestClient<? extends RemoteRestService> get(Class<? extends RemoteRestService> restServiceClass) {
        if (!cacheMap.containsKey(restServiceClass)) {
            cacheMap.put(restServiceClass, clientProxyFactory.createClient(restServiceClass));
        }

        return cacheMap.get(restServiceClass);
    }

    public void remove(Class<? extends RemoteRestService> restServiceClass) {
        if (!cacheMap.containsKey(restServiceClass))
            return;

        cacheMap.remove(restServiceClass);
    }
}
