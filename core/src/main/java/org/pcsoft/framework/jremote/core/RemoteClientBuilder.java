package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.core.internal.registry.ServerClientPluginRegistry;

public final class RemoteClientBuilder implements RemoteBuilder<RemoteClient> {
    public static RemoteClientBuilder create(String host, int port, int ownPort) {
        return new RemoteClientBuilder(host, port, ownPort);
    }

    private final RemoteClient remoteClient;

    private RemoteClientBuilder(String host, int port, int ownPort) {
        remoteClient = new RemoteClient(host, port, ownPort);
        remoteClient.getProxyManager().setRemoteRegistrationClient(ServerClientPluginRegistry.getInstance().getRegistrationServiceClass(), host, port);
        remoteClient.getProxyManager().setRemoteKeepAliveClient(ServerClientPluginRegistry.getInstance().getKeepAliveServiceClass(), host, port);
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

    public final RemoteClientBuilder withRemoteControlClient(Class<?>... controlClientClasses) {
        for (final Class<?> pushServiceClass : controlClientClasses) {
            remoteClient.getProxyManager().addRemoteControlClientProxy(pushServiceClass, remoteClient.getHost(), remoteClient.getPort());
        }
        return this;
    }

    @Override
    public RemoteClient build() {
        return remoteClient;
    }
}
