package org.pcsoft.framework.jremote.core.internal.manager;

import org.pcsoft.framework.jremote.api.type.ChangeListener;
import org.pcsoft.framework.jremote.core.internal.proxy.ProxyFactory;
import org.pcsoft.framework.jremote.core.internal.type.PushMethodKey;
import org.pcsoft.framework.jremote.core.internal.type.wrapper.RemoteKeepAliveClientWrapper;
import org.pcsoft.framework.jremote.core.internal.type.wrapper.RemoteRegistrationClientWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientProxyManager {
    private final Map<Class<?>, Object> modelProxyMap = new HashMap<>();
    private final Map<Class<?>, Object> observerProxyMap = new HashMap<>();
    private final Map<Class<?>, Object> pushServiceProxyMap = new HashMap<>();
    private final Map<Class<?>, Object> controlClientProxyMap = new HashMap<>();
    private RemoteRegistrationClientWrapper remoteRegistrationClient;
    private RemoteKeepAliveClientWrapper remoteKeepAliveClient;

    //Data
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

    //region Control Client Proxy
    public <T> void addRemoteControlClientProxy(Class<T> clazz, String host, int port) {
        if (controlClientProxyMap.containsKey(clazz))
            throw new IllegalStateException("Remote control client class already added: " + clazz.getName());

        final T proxy = ProxyFactory.buildRemoteClientProxy(clazz, host, port);
        controlClientProxyMap.put(clazz, proxy);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRemoteControlClientProxy(Class<T> clazz) {
        if (!controlClientProxyMap.containsKey(clazz))
            throw new IllegalStateException("Unknown remote control client class: " + clazz.getName());

        return (T) controlClientProxyMap.get(clazz);
    }

    public Class[] getRemoteControlClasses() {
        return controlClientProxyMap.keySet().toArray(new Class[0]);
    }
    //endregion

    public RemoteRegistrationClientWrapper getRemoteRegistrationClient() {
        return remoteRegistrationClient;
    }

    public void setRemoteRegistrationClient(Class<?> clazz, String host, int port) {
        this.remoteRegistrationClient = new RemoteRegistrationClientWrapper(ProxyFactory.buildRemoteClientProxy(clazz, host, port));
    }

    public RemoteKeepAliveClientWrapper getRemoteKeepAliveClient() {
        return remoteKeepAliveClient;
    }

    public void setRemoteKeepAliveClient(Class<?> clazz, String host, int port) {
        this.remoteKeepAliveClient = new RemoteKeepAliveClientWrapper(ProxyFactory.buildRemoteClientProxy(clazz, host, port));
    }
}
