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
    }

    @Override
    public RemoteServer build() {
        return remoteServer;
    }
}
