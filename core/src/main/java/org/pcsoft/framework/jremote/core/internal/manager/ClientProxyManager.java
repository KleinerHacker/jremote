package org.pcsoft.framework.jremote.core.internal.manager;

import org.pcsoft.framework.jremote.api.type.EventReceivedListener;
import org.pcsoft.framework.jremote.api.type.PushChangedListener;
import org.pcsoft.framework.jremote.core.internal.proxy.ProxyFactory;
import org.pcsoft.framework.jremote.commons.type.MethodKey;
import org.pcsoft.framework.jremote.core.internal.type.RemoteKeepAliveClientWrapper;
import org.pcsoft.framework.jremote.core.internal.type.RemoteRegistrationClientWrapper;
import org.pcsoft.framework.jremote.ext.np.api.NetworkProtocol;
import org.pcsoft.framework.jremote.ext.up.api.UpdatePolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientProxyManager {
    private final Map<Class<?>, Object> pushModelProxyMap = new HashMap<>();
    private final Map<Class<?>, Object> pushObserverProxyMap = new HashMap<>();
    private final Map<Class<?>, Object> pushServiceProxyMap = new HashMap<>();
    private final Map<Class<?>, Object> eventReceiverProxyMap = new HashMap<>();
    private final Map<Class<?>, Object> eventServiceProxyMap = new HashMap<>();
    private final Map<Class<?>, Object> controlClientProxyMap = new HashMap<>();
    private RemoteRegistrationClientWrapper remoteRegistrationClient;
    private RemoteKeepAliveClientWrapper remoteKeepAliveClient;

    //Data
    private final Map<MethodKey, Object> propertyValueMap = new HashMap<>();
    private final Map<MethodKey, List<PushChangedListener>> pushObserverListenerMap = new HashMap<>();
    private final Map<MethodKey, List<EventReceivedListener>> eventReceiverListenerMap = new HashMap<>();

    //region Push Model Proxy
    public <T> void addRemotePushModelProxy(Class<T> clazz) {
        if (pushModelProxyMap.containsKey(clazz))
            throw new IllegalStateException("Remote push model class already added: " + clazz.getName());

        final T proxy = ProxyFactory.buildRemotePushModelProxy(clazz, propertyValueMap);
        pushModelProxyMap.put(clazz, proxy);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRemotePushModelProxy(Class<T> clazz) {
        if (!pushModelProxyMap.containsKey(clazz))
            throw new IllegalStateException("Unknown remote push model class: " + clazz.getName());

        return (T) pushModelProxyMap.get(clazz);
    }

    public Class[] getRemotePushModelClasses() {
        return pushModelProxyMap.keySet().toArray(new Class[0]);
    }
    //endregion

    //region Push Observer Proxy
    public <T> void addRemotePushObserverProxy(Class<T> clazz) {
        if (pushObserverProxyMap.containsKey(clazz))
            throw new IllegalStateException("Remote push observer class already added: " + clazz.getName());

        final T proxy = ProxyFactory.buildRemotePushObserverProxy(clazz, pushObserverListenerMap);
        pushObserverProxyMap.put(clazz, proxy);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRemotePushObserverProxy(Class<T> clazz) {
        if (!pushObserverProxyMap.containsKey(clazz))
            throw new IllegalStateException("Unknown remote push observer class: " + clazz.getName());

        return (T) pushObserverProxyMap.get(clazz);
    }

    public Class[] getRemotePushObserverClasses() {
        return pushObserverProxyMap.keySet().toArray(new Class[0]);
    }
    //endregion

    //region Push Service Proxy
    public <T> void addRemotePushServiceProxy(Class<T> clazz, UpdatePolicy updatePolicy) {
        if (pushServiceProxyMap.containsKey(clazz))
            throw new IllegalStateException("Remote push service class already added: " + clazz.getName());

        final T proxy = ProxyFactory.buildRemotePushServiceProxy(clazz, propertyValueMap, pushObserverListenerMap, this::getRemotePushModelClasses, updatePolicy);
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

    //region Event Receiver Proxy
    public <T> void addRemoteEventReceiverProxy(Class<T> clazz) {
        if (eventReceiverProxyMap.containsKey(clazz))
            throw new IllegalStateException("Remote event receiver class already added: " + clazz.getName());

        final T proxy = ProxyFactory.buildRemoteEventReceiverProxy(clazz, eventReceiverListenerMap);
        eventReceiverProxyMap.put(clazz, proxy);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRemoteEventReceiverProxy(Class<T> clazz) {
        if (!eventReceiverProxyMap.containsKey(clazz))
            throw new IllegalStateException("Unknown event push receiver class: " + clazz.getName());

        return (T) eventReceiverProxyMap.get(clazz);
    }

    public Class[] getRemoteEventReceiverClasses() {
        return eventReceiverProxyMap.keySet().toArray(new Class[0]);
    }
    //endregion

    //region Event Service Proxy
    public <T> void addRemoteEventServiceProxy(Class<T> clazz, UpdatePolicy updatePolicy) {
        if (eventServiceProxyMap.containsKey(clazz))
            throw new IllegalStateException("Remote event service class already added: " + clazz.getName());

        final T proxy = ProxyFactory.buildRemoteEventServiceProxy(clazz, eventReceiverListenerMap, updatePolicy);
        eventServiceProxyMap.put(clazz, proxy);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRemoteEventServiceProxy(Class<T> clazz) {
        if (!eventServiceProxyMap.containsKey(clazz))
            throw new IllegalStateException("Unknown remote event service class: " + clazz.getName());

        return (T) eventServiceProxyMap.get(clazz);
    }

    public Class[] getRemoteEventClasses() {
        return eventServiceProxyMap.keySet().toArray(new Class[0]);
    }
    //endregion

    //region Control Client Proxy
    public <T> void addRemoteControlClientProxy(Class<T> clazz, String host, int port, NetworkProtocol networkProtocol) {
        if (controlClientProxyMap.containsKey(clazz))
            throw new IllegalStateException("Remote control client class already added: " + clazz.getName());

        final T proxy = ProxyFactory.buildRemoteClientProxy(clazz, host, port, networkProtocol);
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

    public void setupRemoteRegistrationClient(String host, int port, NetworkProtocol networkProtocol) {
        this.remoteRegistrationClient = new RemoteRegistrationClientWrapper(ProxyFactory.buildRemoteClientProxy(
                networkProtocol.getRegistrationServiceClass(), host, port, networkProtocol));
    }

    public RemoteKeepAliveClientWrapper getRemoteKeepAliveClient() {
        return remoteKeepAliveClient;
    }

    public void setupRemoteKeepAliveClient(String host, int port, NetworkProtocol networkProtocol) {
        this.remoteKeepAliveClient = new RemoteKeepAliveClientWrapper(ProxyFactory.buildRemoteClientProxy(
                networkProtocol.getKeepAliveServiceClass(), host, port, networkProtocol));
    }
}
