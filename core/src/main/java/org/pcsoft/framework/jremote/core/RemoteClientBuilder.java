package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.api.config.JRemoteClientConfiguration;
import org.pcsoft.framework.jremote.api.factory.ClientFactory;
import org.pcsoft.framework.jremote.api.factory.ServerFactory;

public final class RemoteClientBuilder extends RemoteBaseBuilder<RemoteClientBuilder> {
    public static RemoteClientBuilder create(final ServerFactory serverFactory, final ClientFactory clientFactory) {
        return new RemoteClientBuilder(serverFactory, clientFactory);
    }

    private JRemoteClientConfiguration configuration = new JRemoteClientConfiguration();

    private RemoteClientBuilder(ServerFactory serverFactory, ClientFactory clientFactory) {
        super(serverFactory, clientFactory);
    }

    public RemoteClientBuilder withConfiguration(final JRemoteClientConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public RemoteClient build() {
        return new RemoteClient(serverFactory, clientFactory, configuration, classList.toArray(new Class[classList.size()]));
    }
}
