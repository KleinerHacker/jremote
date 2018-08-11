package org.pcsoft.framework.jremote.core.internal.manager;

import org.pcsoft.framework.jremote.api.type.ChangeListener;
import org.pcsoft.framework.jremote.core.internal.proxy.ProxyFactory;
import org.pcsoft.framework.jremote.core.internal.type.DefaultService;
import org.pcsoft.framework.jremote.core.internal.type.PushMethodKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientProxyManager {
    private final Map<Class<?>, Object> modelProxyMap = new HashMap<>();
    private final Map<Class<?>, Object> observerProxyMap = new HashMap<>();
    private final Map<Class<?>, Object> pushServiceProxyMap = new HashMap<>();
    private final Map<DefaultService, Object> defaultClientProxyMap = new HashMap<>();

    private final Map<PushMethodKey, Object> propertyValueMap = new HashMap<>();
    private final Map<PushMethodKey, List<ChangeListener>> observerListenerMap = new HashMap<>();

    //region Model Proxy
    public <T> void addRemoteModelProxy(Class<T> clazz) {
        if (modelProxyMap.containsKey(clazz))
            throw new IllegalStateException("Remote model class already added: " + clazz.getName());

        final T proxy = ProxyFactory.buildRemoteModelProxy(clazz, propertyValueMap);
        modelProxyMap.put(clazz, proxy);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRemoteModelProxy(Class<T> clazz) {
        if (!modelProxyMap.containsKey(clazz))
            throw new IllegalStateException("Unknown remote model class: " + clazz.getName());

        return (T) modelProxyMap.get(clazz);
    }

    public Class[] getRemoteModelClasses() {
        return modelProxyMap.keySet().toArray(new Class[0]);
    }
    //endregion

    //region Observer Proxy
    public <T> void addRemoteObserverProxy(Class<T> clazz) {
        if (observerProxyMap.containsKey(clazz))
            throw new IllegalStateException("Remote observer class already added: " + clazz.getName());

        final T proxy = ProxyFactory.buildRemoteObserverProxy(clazz, observerListenerMap);
        observerProxyMap.put(clazz, proxy);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRemoteObserverProxy(Class<T> clazz) {
        if (!observerProxyMap.containsKey(clazz))
            throw new IllegalStateException("Unknown remote observer class: " + clazz.getName());

        return (T) observerProxyMap.get(clazz);
    }

    public Class[] getRemoteObserverClasses() {
        return observerProxyMap.keySet().toArray(new Class[0]);
    }
    //endregion

    //region Push Service Proxy
    public <T> void addRemotePushServiceProxy(Class<T> clazz) {
        if (pushServiceProxyMap.containsKey(clazz))
            throw new IllegalStateException("Remote push service class already added: " + clazz.getName());

        final T proxy = ProxyFactory.buildRemotePushServiceProxy(clazz, propertyValueMap, observerListenerMap);
        pushServiceProxyMap.put(clazz, proxy);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRemotePushServiceProxy(Class<T> clazz) {
        if (!pushServiceProxyMap.containsKey(clazz))
            throw new IllegalStateException("Unknown remote push service class: " + clazz.getName());

        return (T) pushServiceProxyMap.get(clazz);
    }

    public Class[] getRemotePushClasses() {
        return pushServiceProxyMap.keySet().toArray(new Class[0]);
    }
    //endregion

    //region Default Client Proxy
    public <T> void addRemoteDefaultClientProxy(DefaultService defaultService, Class<T> clazz) {
        if (defaultClientProxyMap.containsKey(defaultService))
            throw new IllegalStateException("Remote default client class already added: " + defaultService + " (" + clazz.getName() + ")");

        final T proxy;
        switch (defaultService) {
            case KeepAlive:
                proxy = ProxyFactory.buildRemoteKeepAliveClientProxy(clazz);
                break;
            case Registration:
                proxy = ProxyFactory.buildRemoteClientProxy(clazz);
                break;
            default:
                throw new RuntimeException();
        }
        defaultClientProxyMap.put(defaultService, proxy);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRemoteDefaultClientProxy(DefaultService defaultService) {
        if (!defaultClientProxyMap.containsKey(defaultService))
            throw new IllegalStateException("Unknown remote default client: " + defaultService);

        return (T) defaultClientProxyMap.get(defaultService);
    }

    public DefaultService[] getDefaultClients() {
        return defaultClientProxyMap.keySet().toArray(new DefaultService[0]);
    }
    //endregion
}
