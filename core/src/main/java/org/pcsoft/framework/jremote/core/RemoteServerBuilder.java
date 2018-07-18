package org.pcsoft.framework.jremote.core;

public final class RemoteServerBuilder implements RemoteBuilder<RemoteServer> {
    public static RemoteServerBuilder create() {
        return new RemoteServerBuilder();
    }

    private final RemoteServer remoteServer;

    private RemoteServerBuilder() {
        remoteServer = new RemoteServer();
    }

    @Override
    public RemoteServer build() {
        return remoteServer;
    }
}
