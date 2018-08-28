package org.pcsoft.framework.jremote.ext.config.impl.fluent;

import org.pcsoft.framework.jremote.ext.config.api.ServerConfiguration;

public final class ServerFluentConfiguration extends RemoteFluentConfiguration<ServerFluentConfiguration> implements ServerConfiguration {
    public static ServerFluentConfiguration create() {
        return new ServerFluentConfiguration();
    }

    private ServerFluentConfiguration() {
    }
}
