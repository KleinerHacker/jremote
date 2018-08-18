package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.type.EventChangeListener;
import org.pcsoft.framework.jremote.api.type.PushChangeListener;
import org.pcsoft.framework.jremote.core.internal.registry.ClientRegistry;
import org.pcsoft.framework.jremote.core.internal.type.MethodKey;

import java.util.List;
import java.util.Map;

public final class ProxyFactory {

    public static <T> T buildRemotePushModelProxy(Class<T> clazz, Map<MethodKey, Object> dataMap) {
        return RemotePushModelProxyBuilder.getInstance().buildProxy(clazz, dataMap);
    }

    public static <T> T buildRemotePushObserverProxy(Class<T> clazz, Map<MethodKey, List<PushChangeListener>> listenerMap) {
        return RemotePushObserverProxyBuilder.getInstance().buildProxy(clazz, listenerMap);
    }

    public static <T> T buildRemoteEventObserverProxy(Class<T> clazz, Map<MethodKey, List<EventChangeListener>> listenerMap) {
        return RemoteEventObserverProxyBuilder.getInstance().buildProxy(clazz, listenerMap);
    }

    public static <T> T buildRemotePushServiceProxy(Class<T> clazz, Map<MethodKey, Object> dataMap, Map<MethodKey, List<PushChangeListener>> listenerMap) {
        return RemotePushServiceProxyBuilder.getInstance().buildProxy(clazz, new RemotePushServiceProxyBuilder.DataHolder(dataMap, listenerMap));
    }

    public static <T> T buildRemoteEventServiceProxy(Class<T> clazz, Map<MethodKey, List<EventChangeListener>> listenerMap) {
        return RemoteEventServiceProxyBuilder.getInstance().buildProxy(clazz, listenerMap);
    }

    public static <T> T buildRemoteClientProxy(Class<T> clazz, String host, int port) {
        return RemoteClientServiceProxyBuilder.buildProxy(clazz, host, port);
    }

    public static <T> T buildRemoteBroadcastClientProxy(Class<T> clazz, ClientRegistry clientRegistry) {
        return RemoteClientServiceProxyBuilder.buildBroadcastProxy(clazz, clientRegistry);
    }

    public static <T> T buildRemoteRegistrationServiceProxy(Class<T> clazz, ClientRegistry clientRegistry) {
        return RemoteRegistrationServiceProxyBuilder.getInstance().buildProxy(clazz, clientRegistry);
    }

    public static <T> T buildRemoteKeepAliveServiceProxy(Class<T> clazz, ClientRegistry clientRegistry) {
        return RemoteKeepAliveServiceProxyBuilder.getInstance().buildProxy(clazz, clientRegistry);
    }

    private ProxyFactory() {
    }
}
