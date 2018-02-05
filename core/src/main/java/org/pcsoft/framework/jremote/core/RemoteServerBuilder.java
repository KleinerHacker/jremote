package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.api.config.JRemoteServerConfiguration;
import org.pcsoft.framework.jremote.api.factory.ClientFactory;
import org.pcsoft.framework.jremote.api.factory.ServerFactory;

public final class RemoteServerBuilder extends RemoteBaseBuilder<RemoteServerBuilder> {
    private JRemoteServerConfiguration configuration = new JRemoteServerConfiguration();

    private RemoteServerBuilder(ServerFactory serverFactory, ClientFactory clientFactory) {
        super(serverFactory, clientFactory);
    }

    public RemoteServerBuilder withConfiguration(final JRemoteServerConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public RemoteServer build() {
        return new RemoteServer(serverFactory, clientFactory, configuration, classList.toArray(new Class[classList.size()]));
    }
}
