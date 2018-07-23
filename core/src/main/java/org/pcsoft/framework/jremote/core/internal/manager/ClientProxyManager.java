package org.pcsoft.framework.jremote.core.internal.manager;

import org.pcsoft.framework.jremote.api.type.ChangeListener;
import org.pcsoft.framework.jremote.core.internal.proxy.ProxyFactory;
import org.pcsoft.framework.jremote.core.internal.type.PushMethodKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientProxyManager {
    private final Map<Class<?>, Object> modelProxyMap = new HashMap<>();
    private final Map<Class<?>, Object> observerProxyMap = new HashMap<>();

    private final Map<PushMethodKey, Object> propertyValueMap = new HashMap<>();
    private final Map<PushMethodKey, List<ChangeListener>> observerListenerMap = new HashMap<>();

    //region Model Proxy
    public <T>void addRemoteModelProxy(Class<T> clazz) {
        if (modelProxyMap.containsKey(clazz))
            throw new IllegalStateException("Remote model class already added: " + clazz.getName());

        final T proxy = ProxyFactory.buildRemoteModelProxy(clazz, propertyValueMap);
        modelProxyMap.put(clazz, proxy);
    }

    @SuppressWarnings("unchecked")
    public <T>T getRemoteModelProxy(Class<T> clazz) {
        if (!modelProxyMap.containsKey(clazz))
            throw new IllegalStateException("Unknown remote model class: " + clazz.getName());

        return (T) modelProxyMap.get(clazz);
    }
    //endregion

    //region Observer Proxy
    public <T>void addRemoteObserverProxy(Class<T> clazz) {
        if (observerProxyMap.containsKey(clazz))
            throw new IllegalStateException("Remote observer class already added: " + clazz.getName());

        final T proxy = ProxyFactory.buildRemoteObserverProxy(clazz, observerListenerMap);
        observerProxyMap.put(clazz, proxy);
    }

    @SuppressWarnings("unchecked")
    public <T>T getRemoteObserverProxy(Class<T> clazz) {
        if (!observerProxyMap.containsKey(clazz))
            throw new IllegalStateException("Unknown remote observer class: " + clazz.getName());

        return (T) observerProxyMap.get(clazz);
    }
    //endregion
}
