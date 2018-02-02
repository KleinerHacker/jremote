package org.pcsoft.framework.jremote.core.jersey.internal;

import org.pcsoft.framework.jremote.api.interf.RemoteRestClient;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;
import org.pcsoft.framework.jremote.api.internal.ClientProxyFactory;

import java.lang.reflect.Proxy;

public final class JerseyClientProxyFactory implements ClientProxyFactory {
    private static final JerseyClientProxyFactory INSTANCE = new JerseyClientProxyFactory();

    public static JerseyClientProxyFactory getInstance() {
        return INSTANCE;
    }

    private JerseyClientProxyFactory() {
    }

    @Override
    public <T extends RemoteRestService>T createProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            return null;
        });
    }

    @Override
    public <T extends RemoteRestService> RemoteRestClient<T> createClient(Class<T> clazz) {
        return new JerseyRemoteRestClient<>(createProxy(clazz));
    }
}
