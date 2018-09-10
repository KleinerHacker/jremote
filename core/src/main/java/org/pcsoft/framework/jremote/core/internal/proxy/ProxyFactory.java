package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.type.EventReceivedListener;
import org.pcsoft.framework.jremote.api.type.PushChangedListener;
import org.pcsoft.framework.jremote.core.internal.registry.ClientRegistry;
import org.pcsoft.framework.jremote.commons.type.MethodKey;
import org.pcsoft.framework.jremote.ext.np.api.NetworkProtocol;
import org.pcsoft.framework.jremote.ext.up.api.UpdatePolicy;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ProxyFactory {

    public static <T> T buildRemotePushModelProxy(Class<T> clazz, Map<MethodKey, Object> dataMap) {
        return RemotePushModelProxyBuilder.getInstance().buildProxy(clazz, dataMap);
    }

    public static <T> T buildRemotePushObserverProxy(Class<T> clazz, Map<MethodKey, List<PushChangedListener>> listenerMap) {
        return RemotePushObserverProxyBuilder.getInstance().buildProxy(clazz, listenerMap);
    }

    public static <T> T buildRemoteEventReceiverProxy(Class<T> clazz, Map<MethodKey, List<EventReceivedListener>> listenerMap) {
        return RemoteEventReceiverProxyBuilder.getInstance().buildProxy(clazz, listenerMap);
    }

    public static <T> T buildRemotePushServiceProxy(Class<T> clazz, Map<MethodKey, Object> dataMap, Map<MethodKey, List<PushChangedListener>> listenerMap,
                                                    Supplier<Class<?>[]> modelClassesSupplier, UpdatePolicy updatePolicy) {
        return RemotePushServiceProxyBuilder.getInstance().buildProxy(clazz, new RemotePushServiceProxyBuilder.DataHolder(
                dataMap, listenerMap, modelClassesSupplier, updatePolicy));
    }

    public static <T> T buildRemoteEventServiceProxy(Class<T> clazz, Map<MethodKey, List<EventReceivedListener>> listenerMap, UpdatePolicy updatePolicy) {
        return RemoteEventServiceProxyBuilder.getInstance().buildProxy(clazz, new RemoteEventServiceProxyBuilder.DataHolder(listenerMap, updatePolicy));
    }

    public static <T> T buildRemoteClientProxy(Class<T> clazz, String host, int port, NetworkProtocol networkProtocol) {
        return RemoteClientServiceProxyBuilder.buildProxy(clazz, host, port, networkProtocol);
    }

    public static <T> T buildRemoteBroadcastClientProxy(Class<T> clazz, ClientRegistry clientRegistry, NetworkProtocol networkProtocol) {
        return RemoteClientServiceProxyBuilder.buildBroadcastProxy(clazz, clientRegistry, networkProtocol);
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
