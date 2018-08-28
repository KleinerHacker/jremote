package org.pcsoft.framework.jremote.ext.config.impl.prop;

import org.pcsoft.framework.jremote.ext.config.api.ClientConfiguration;
import org.pcsoft.framework.jremote.ext.config.api.exception.JRemoteConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ClientPropertyConfiguration extends RemotePropertyConfiguration implements ClientConfiguration {
    private static final String KEY_OWN_PORT = "remote.own_port";

    public static ClientPropertyConfiguration loadFrom(File file) throws IOException {
        try (final InputStream in = new FileInputStream(file)) {
            return loadFrom(in);
        }
    }

    public static ClientPropertyConfiguration loadFrom(InputStream in) throws IOException {
        final Properties properties = new Properties();
        properties.load(in);

        return loadFrom(properties);
    }

    public static ClientPropertyConfiguration loadFrom(Properties properties) {
        final ClientPropertyConfiguration configuration = new ClientPropertyConfiguration();
        configuration.load(properties);

        return configuration;
    }

    private int ownPort = 0;

    private ClientPropertyConfiguration() {
    }

    @Override
    public int getOwnPort() {
        return ownPort;
    }

    @Override
    public void validate() throws JRemoteConfigurationException {
        super.validate();
        if (ownPort <= 0)
            throw new JRemoteConfigurationException("Own port not set in configuration");
    }

    @Override
    protected void load(Properties properties) throws JRemoteConfigurationException {
        super.load(properties);
        try {
            ownPort = Integer.parseInt(properties.getProperty(KEY_OWN_PORT));
        } catch (NumberFormatException e) {
            throw new JRemoteConfigurationException("Unable to parse key " + KEY_OWN_PORT + " as integer", e);
        }
    }
}
