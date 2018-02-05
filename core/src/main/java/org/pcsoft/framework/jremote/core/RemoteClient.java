package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.api.config.JRemoteClientConfiguration;
import org.pcsoft.framework.jremote.api.factory.ClientFactory;
import org.pcsoft.framework.jremote.api.factory.ServerFactory;
import org.pcsoft.framework.jremote.api.interf.RemoteRestClient;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;
import org.pcsoft.framework.jremote.api.type.ClientConnection;

public final class RemoteClient extends RemoteBase {
    private final JRemoteClientConfiguration configuration;
    private final ClientConnection clientConnection;

    RemoteClient(final ServerFactory serverFactory, final ClientFactory clientFactory, final JRemoteClientConfiguration configuration,
                 final Class<? extends RemoteRestService>[] restServiceClasses) {
        super(serverFactory, clientFactory, configuration.getLocalUri(), restServiceClasses);
        this.configuration = configuration;

        this.clientConnection = clientFactory.createClientConnection(configuration.getRemoteUri());
    }

    public <T extends RemoteRestService> RemoteRestClient<T> getClient(final Class<T> clazz) {
        final T proxyClient = clientFactory.createClient(clazz, configuration.getRemoteUri(), clientConnection);
        return new RemoteRestClient<>(proxyClient);
    }
}
