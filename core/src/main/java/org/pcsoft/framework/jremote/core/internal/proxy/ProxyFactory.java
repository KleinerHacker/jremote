package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.type.ChangeListener;
import org.pcsoft.framework.jremote.core.internal.type.PushMethodKey;

import java.util.List;
import java.util.Map;

public final class ProxyFactory {

    public static <T>T buildRemoteModelProxy(Class<T> clazz, Map<PushMethodKey, Object> dataMap) {
        return RemoteModelProxyBuilder.getInstance().buildProxy(clazz, dataMap);
    }

    public static <T>T buildRemoteObserverProxy(Class<T> clazz, Map<PushMethodKey, List<ChangeListener>> listenerMap) {
        return RemoteObserverProxyBuilder.getInstance().buildProxy(clazz, listenerMap);
    }

    public static <T>T buildRemotePushServiceProxy(Class<T> clazz, Map<PushMethodKey, Object> dataMap, Map<PushMethodKey, List<ChangeListener>> listenerMap) {
        return RemotePushServiceProxyBuilder.getInstance().buildProxy(clazz, new RemotePushServiceProxyBuilder.DataHolder(dataMap, listenerMap));
    }

    public static <T>T buildRemoteClientProxy(Class<T> clazz, String host, int port) {
        return RemoteClientServiceProxyBuilder.buildProxy(clazz, host, port);
    }

    private ProxyFactory() {
    }
}
