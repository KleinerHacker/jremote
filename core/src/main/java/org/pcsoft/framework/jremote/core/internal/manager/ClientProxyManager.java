package org.pcsoft.framework.jremote.core.internal.manager;

import org.pcsoft.framework.jremote.core.internal.proxy.ProxyFactory;
import org.pcsoft.framework.jremote.core.internal.type.PushMethodKey;

import java.util.HashMap;
import java.util.Map;

public final class ClientProxyManager {
    private final Map<Class<?>, Object> modelProxyMap = new HashMap<>();

    private final Map<PushMethodKey, Object> propertyValueMap = new HashMap<>();

    public <T>void addRemoteModelProxy(Class<T> clazz) {
        if (modelProxyMap.containsKey(clazz))
            throw new IllegalStateException("Remote model class already added: " + clazz.getName());

        final T proxy = ProxyFactory.buildRemoteModelProxy(clazz, propertyValueMap);
        modelProxyMap.put(clazz, proxy);
    }

    @SuppressWarnings("unchecked")
    public <T>T geRemoteModelProxy(Class<T> clazz) {
        if (!modelProxyMap.containsKey(clazz))
            throw new IllegalStateException("Unknown remote model class: " + clazz.getName());

        return (T) modelProxyMap.get(clazz);
    }
}
