package org.pcsoft.framework.jremote.core;

public final class RemoteClientBuilder implements RemoteBuilder<RemoteClient> {
    public static RemoteClientBuilder create() {
        return new RemoteClientBuilder();
    }

    private final RemoteClient remoteClient;

    private RemoteClientBuilder() {
        remoteClient = new RemoteClient();
    }

    public RemoteClientBuilder withRemoteModel(Class<?> modelClass) {
        remoteClient.getProxyManager().addRemoteModelProxy(modelClass);
        return this;
    }

    @Override
    public RemoteClient build() {
        return remoteClient;
    }
}
