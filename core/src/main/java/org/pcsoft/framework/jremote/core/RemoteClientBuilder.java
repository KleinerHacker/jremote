package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.core.internal.proxy.ProxyFactory;
import org.pcsoft.framework.jremote.core.internal.registry.ServerClientPluginRegistry;
import org.pcsoft.framework.jremote.core.internal.type.wrapper.RemoteKeepAliveClientWrapper;
import org.pcsoft.framework.jremote.core.internal.type.wrapper.RemoteRegistrationClientWrapper;

public final class RemoteClientBuilder implements RemoteBuilder<RemoteClient> {
    public static RemoteClientBuilder create(String host, int port) {
        return new RemoteClientBuilder(host, port);
    }

    private final RemoteClient remoteClient;

    private RemoteClientBuilder(String host, int port) {
        remoteClient = new RemoteClient(host, port);
        remoteClient.getProxyManager().setRemoteRegistrationClient(new RemoteRegistrationClientWrapper(
                ProxyFactory.buildRemoteClientProxy(ServerClientPluginRegistry.getInstance().getRegistrationServiceClass(), host, port)
        ));
        remoteClient.getProxyManager().setRemoteKeepAliveClient(new RemoteKeepAliveClientWrapper(
                ProxyFactory.buildRemoteClientProxy(ServerClientPluginRegistry.getInstance().getKeepAliveServiceClass(), host, port)
        ));
    }

    public final RemoteClientBuilder withRemoteModel(Class<?>... modelClasses) {
        for (final Class<?> modelClass : modelClasses) {
            remoteClient.getProxyManager().addRemoteModelProxy(modelClass);
        }
        return this;
    }

    public final RemoteClientBuilder withRemoteObserver(Class<?>... observerClasses) {
        for (final Class<?> observerClass : observerClasses) {
            remoteClient.getProxyManager().addRemoteObserverProxy(observerClass);
        }
        return this;
    }

    public final RemoteClientBuilder withRemotePushService(Class<?>... pushServiceClasses) {
        for (final Class<?> pushServiceClass : pushServiceClasses) {
            remoteClient.getProxyManager().addRemotePushServiceProxy(pushServiceClass);
        }
        return this;
    }

    @Override
    public RemoteClient build() {
        return remoteClient;
    }
}
