package org.pcsoft.framework.jremote.ext.config.impl.prop;

import org.apache.commons.lang3.StringUtils;
import org.pcsoft.framework.jremote.ext.config.api.RemoteConfiguration;
import org.pcsoft.framework.jremote.ext.config.api.exception.JRemoteConfigurationException;

import java.util.Properties;

public abstract class RemotePropertyConfiguration implements RemoteConfiguration {
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

    protected void load(Properties properties) throws JRemoteConfigurationException {
        host = properties.getProperty(KEY_HOST);
        try {
            port = Integer.parseInt(properties.getProperty(KEY_PORT));
        } catch (NumberFormatException e) {
            throw new JRemoteConfigurationException("Unable to parse key " + KEY_PORT + " as integer", e);
        }
    }
}
