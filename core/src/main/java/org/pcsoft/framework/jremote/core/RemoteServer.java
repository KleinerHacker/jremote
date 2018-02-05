package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.api.config.JRemoteServerConfiguration;
import org.pcsoft.framework.jremote.api.factory.ClientFactory;
import org.pcsoft.framework.jremote.api.factory.ServerFactory;
import org.pcsoft.framework.jremote.api.interf.RemoteRestClientBroadcast;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;
import org.pcsoft.framework.jremote.api.type.ClientConnection;
import org.pcsoft.framework.jremote.core.internal.ClientRegister;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public final class RemoteServer extends RemoteBase {
    private final ClientRegister clientRegister = new ClientRegister(clientFactory);
    private final JRemoteServerConfiguration configuration;

    RemoteServer(ServerFactory serverFactory, ClientFactory clientFactory, JRemoteServerConfiguration configuration, Class<? extends RemoteRestService>[] restServiceClasses) {
        super(serverFactory, clientFactory, configuration.getLocalUri(), restServiceClasses);
        this.configuration = configuration;
    }

    public <T extends RemoteRestService> RemoteRestClientBroadcast<T> getClient(Class<T> clazz) {
        final List<T> proxyList = new ArrayList<>();
        for (final String address : clientRegister.getClientAddresses()) {
            final ClientConnection clientConnection = clientRegister.getClient(address);
            final T proxyClient = clientFactory.createClient(clazz, URI.create(address), clientConnection);

            proxyList.add(proxyClient);
        }

        return new RemoteRestClientBroadcast<>(clazz, proxyList);
    }
}
