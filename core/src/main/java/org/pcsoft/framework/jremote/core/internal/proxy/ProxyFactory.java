package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.type.ChangeListener;
import org.pcsoft.framework.jremote.core.internal.type.PushMethodKey;

import java.util.List;
import java.util.Map;

public final class ProxyFactory {

    public static <T>T buildRemoteModelProxy(Class<T> clazz, Map<PushMethodKey, Object> dataMap) {
        return RemoteModelProxyBuilder.buildProxy(clazz, dataMap);
    }

    public static <T>T buildRemoteObserverProxy(Class<T> clazz, Map<PushMethodKey, List<ChangeListener>> dataMap) {
        return RemoteObserverProxyBuilder.buildProxy(clazz, dataMap);
    }

    private ProxyFactory() {
    }
}
