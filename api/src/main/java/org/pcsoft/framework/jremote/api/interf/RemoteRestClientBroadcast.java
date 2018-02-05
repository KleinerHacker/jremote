package org.pcsoft.framework.jremote.api.interf;

import java.lang.reflect.Proxy;
import java.util.Collection;

/**
 * Class for a remote REST client, based on the used {@link RemoteRestService}
 * @param <T> REST service to connect as client
 */
public final class RemoteRestClientBroadcast<T extends RemoteRestService> {
    private final T proxyService;

    @SuppressWarnings("unchecked")
    public RemoteRestClientBroadcast(Class<T> clazz, Collection<T> proxyServices) {
        this.proxyService = (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            for (final T proxyService : proxyServices) {
                method.invoke(proxyService, args);
            }

            return null;
        });
    }

    /**
     * Returns the generated proxy object for communication
     * @return
     */
    T get() {
        return this.proxyService;
    }
}
