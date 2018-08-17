package org.pcsoft.framework.jremote.core.internal.manager;

import org.pcsoft.framework.jremote.core.Client;
import org.pcsoft.framework.jremote.core.internal.proxy.ProxyFactory;
import org.pcsoft.framework.jremote.core.internal.registry.ClientRegistry;
import org.pcsoft.framework.jremote.core.internal.handler.PushModelHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class ServerProxyManager {
    private final Map<Class<?>, Object> controlServiceImplMap = new HashMap<>();
    private final Map<Class<?>, Object> pushClientProxyMap = new HashMap<>();
    private Object registrationServiceProxy;
    private Object keepAliveServiceProxy;
    private final Map<Class<?>, PushModelHandler> modelHandlerMap = new HashMap<>();

    //Data
    private final ClientRegistry clientRegistry = new ClientRegistry();

    //region Control Client Proxy
    public <T> void addRemoteControlServiceImpl(Class<T> clazz, Object impl) {
        if (controlServiceImplMap.containsKey(clazz))
            throw new IllegalStateException("Remote control service class already added: " + clazz.getName());

        controlServiceImplMap.put(clazz, impl);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRemoteControlServiceImpl(Class<T> clazz) {
        if (!controlServiceImplMap.containsKey(clazz))
            throw new IllegalStateException("Unknown remote control service class: " + clazz.getName());

        return (T) controlServiceImplMap.get(clazz);
    }

    public Class[] getRemoteControlClasses() {
        return controlServiceImplMap.keySet().toArray(new Class[0]);
    }
    //endregion

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

    //region Model Data
    public <T> void addModelHandler(Class<T> clazz, Object impl) {
        if (modelHandlerMap.containsKey(clazz))
            throw new IllegalStateException("Model data class already added: " + clazz.getName());

        modelHandlerMap.put(clazz, new PushModelHandler(impl));
    }

    @SuppressWarnings("unchecked")
    public PushModelHandler getModelHandler(Class<?> clazz) {
        if (!modelHandlerMap.containsKey(clazz))
            throw new IllegalStateException("Unknown model data class: " + clazz.getName());

        return modelHandlerMap.get(clazz);
    }

    public Class[] getModelHandlerClasses() {
        return modelHandlerMap.keySet().toArray(new Class[0]);
    }
    //endregion

    public <T> void setRemoteRegistrationServiceProxy(Class<T> clazz) {
        registrationServiceProxy = ProxyFactory.buildRemoteRegistrationServiceProxy(clazz, clientRegistry);
    }

    public Object getRemoteRegistrationServiceProxy() {
        return registrationServiceProxy;
    }

    public <T> void setRemoteKeepAliveServiceProxy(Class<T> clazz) {
        keepAliveServiceProxy = ProxyFactory.buildRemoteKeepAliveServiceProxy(clazz, clientRegistry);
    }

    public Object getRemoteKeepAliveServiceProxy() {
        return keepAliveServiceProxy;
    }

    //region Delegates
    public Client[] getConnectedClients() {
        return clientRegistry.getClients().clone();
    }

    public void addClientRegisteredListener(Consumer<Client> l) {
        clientRegistry.addClientRegisteredListener(l);
    }

    public void removeClientRegisteredListener(Consumer<Client> l) {
        clientRegistry.removeClientRegisteredListener(l);
    }

    public void addClientUnregisteredListener(Consumer<Client> l) {
        clientRegistry.addClientUnregisteredListener(l);
    }

    public void removeClientUnregisteredListener(Consumer<Client> l) {
        clientRegistry.removeClientUnregisteredListener(l);
    }
    //endregion
}
