package org.pcsoft.framework.jremote.ext.config.impl.fluent;

import org.pcsoft.framework.jremote.ext.config.api.ClientConfiguration;
import org.pcsoft.framework.jremote.ext.config.api.exception.JRemoteConfigurationException;

public final class ClientFluentConfiguration extends RemoteFluentConfiguration<ClientFluentConfiguration> implements ClientConfiguration {
    public static ClientFluentConfiguration create() {
        return new ClientFluentConfiguration();
    }

    private int ownPort = 0;

    private ClientFluentConfiguration() {
    }

    @Override
    public int getOwnPort() {
        return ownPort;
    }

    public ClientFluentConfiguration setOwnPort(int ownPort) {
        this.ownPort = ownPort;
        return this;
    }

    @Override
    public void validate() throws JRemoteConfigurationException {
        super.validate();
        if (ownPort <= 0)
            throw new JRemoteConfigurationException("Own port not set in configuration");
    }
}
