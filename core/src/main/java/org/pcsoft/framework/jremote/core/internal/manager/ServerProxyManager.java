package org.pcsoft.framework.jremote.core.internal.manager;

import org.pcsoft.framework.jremote.core.internal.proxy.ProxyFactory;
import org.pcsoft.framework.jremote.core.internal.registry.ClientRegistry;

import java.util.HashMap;
import java.util.Map;

public final class ServerProxyManager {
    private final Map<Class<?>, Object> pushClientProxyMap = new HashMap<>();
    private Object registrationServiceProxy;
    private Object keepAliveServiceProxy;

    //Data
    private final ClientRegistry clientRegistry = new ClientRegistry();

    //region Push Client Proxy
    public <T> void addRemotePushClientProxy(Class<T> clazz) {
        if (pushClientProxyMap.containsKey(clazz))
            throw new IllegalStateException("Remote push client class already added: " + clazz.getName());

        final T proxy = ProxyFactory.buildRemoteBroadcastClientProxy(clazz, clientRegistry);
        pushClientProxyMap.put(clazz, proxy);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRemotePushClientProxy(Class<T> clazz) {
        if (!pushClientProxyMap.containsKey(clazz))
            throw new IllegalStateException("Unknown remote push client class: " + clazz.getName());

        return (T) pushClientProxyMap.get(clazz);
    }

    public Class[] getRemotePushClasses() {
        return pushClientProxyMap.keySet().toArray(new Class[0]);
    }
    //endregion

    public <T> void setRemoteRegistrationServiceProxy(Class<T> clazz) {
        registrationServiceProxy = ProxyFactory.buildRemoteRegistrationServiceProxy(clazz, clientRegistry);
    }

    public <T> void setRemoteKeepAliveServiceProxy(Class<T> clazz) {
        registrationServiceProxy = ProxyFactory.buildRemoteKeepAliveServiceProxy(clazz, clientRegistry);
    }
}
