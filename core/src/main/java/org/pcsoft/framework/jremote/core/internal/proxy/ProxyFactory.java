package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.type.ChangeListener;
import org.pcsoft.framework.jremote.core.internal.registry.ClientRegistry;
import org.pcsoft.framework.jremote.core.internal.type.PushMethodKey;

import java.util.List;
import java.util.Map;

public final class ProxyFactory {

    public static <T>T buildRemoteModelProxy(Class<T> clazz, Map<PushMethodKey, Object> dataMap) {
        return RemoteModelProxyBuilder.buildProxy(clazz, dataMap);
    }

    public static <T>T buildRemoteObserverProxy(Class<T> clazz, Map<PushMethodKey, List<ChangeListener>> listenerMap) {
        return RemoteObserverProxyBuilder.buildProxy(clazz, listenerMap);
    }

    public static <T>T buildRemotePushServiceProxy(Class<T> clazz, Map<PushMethodKey, Object> dataMap, Map<PushMethodKey, List<ChangeListener>> listenerMap) {
        return RemotePushServiceProxyBuilder.buildProxy(clazz, dataMap, listenerMap);
    }

    public static <T>T buildRemoteRegistrationClientProxy(Class<T> clazz) {
        return RemoteRegistrationServiceProxyBuilder.buildProxy(clazz);
    }

    public static <T>T buildRemoteKeepAliveClientProxy(Class<T> clazz) {

    }

    private ProxyFactory() {
    }
}
