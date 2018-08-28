package org.pcsoft.framework.jremote.ext.config.impl.xml;

import org.apache.commons.lang3.StringUtils;
import org.pcsoft.framework.jremote.ext.config.api.RemoteConfiguration;
import org.pcsoft.framework.jremote.ext.config.api.exception.JRemoteConfigurationException;

public abstract class RemoteXmlConfiguration<T extends RemoteConfigurationType> implements RemoteConfiguration {
    private static final String KEY_HOST = "remote.host";
    private static final String KEY_PORT = "remote.port";

    private String host = null;
    private int port = 0;

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void validate() throws JRemoteConfigurationException {
        if (StringUtils.isEmpty(host))
            throw new JRemoteConfigurationException("Host not set in configuration");
        if (port <= 0)
            throw new JRemoteConfigurationException("Port not set in configuration");
    }

    protected void load(T type) throws JRemoteConfigurationException {
        host = type.getHost();
        port = type.getPort();
    }
}
