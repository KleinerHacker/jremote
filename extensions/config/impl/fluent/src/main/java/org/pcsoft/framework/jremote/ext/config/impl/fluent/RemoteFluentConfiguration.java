package org.pcsoft.framework.jremote.ext.config.impl.fluent;

import org.apache.commons.lang3.StringUtils;
import org.pcsoft.framework.jremote.ext.config.api.RemoteConfiguration;
import org.pcsoft.framework.jremote.ext.config.api.exception.JRemoteConfigurationException;

public abstract class RemoteFluentConfiguration<T extends RemoteFluentConfiguration<T>> implements RemoteConfiguration {
    private String host = null;
    private int port = 0;

    @Override
    public String getHost() {
        return host;
    }

    @SuppressWarnings("unchecked")
    public T setHost(String host) {
        this.host = host;
        return (T) this;
    }

    @Override
    public int getPort() {
        return port;
    }

    @SuppressWarnings("unchecked")
    public T setPort(int port) {
        this.port = port;
        return (T)this;
    }

    @Override
    public void validate() throws JRemoteConfigurationException {
        if (StringUtils.isEmpty(host))
            throw new JRemoteConfigurationException("Host not set in configuration");
        if (port <= 0)
            throw new JRemoteConfigurationException("Port not set in configuration");
    }
}
