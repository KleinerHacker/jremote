package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.core.internal.registry.ServerClientPluginRegistry;

public final class RemoteServerBuilder implements RemoteBuilder<RemoteServer> {
    public static RemoteServerBuilder create(String host, int port) {
        return new RemoteServerBuilder(host, port);
    }

    private final RemoteServer remoteServer;

    private RemoteServerBuilder(String host, int port) {
        remoteServer = new RemoteServer(host, port);
        remoteServer.getProxyManager().setRemoteRegistrationServiceProxy(
                ServerClientPluginRegistry.getInstance().getRegistrationServiceClass()
        );
        remoteServer.getProxyManager().setRemoteKeepAliveServiceProxy(
                ServerClientPluginRegistry.getInstance().getKeepAliveServiceClass()
        );
    }

    public RemoteServerBuilder withPushClient(Class<?>... pushClientClasses) {
        for (final Class<?> clazz : pushClientClasses) {
            remoteServer.getProxyManager().addRemotePushClientProxy(clazz);
        }
        return this;
    }

    @Override
    public RemoteServer build() {
        return remoteServer;
    }
}
