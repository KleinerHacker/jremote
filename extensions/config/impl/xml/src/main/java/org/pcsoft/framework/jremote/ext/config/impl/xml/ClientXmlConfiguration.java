package org.pcsoft.framework.jremote.ext.config.impl.xml;

import org.pcsoft.framework.jremote.ext.config.api.ClientConfiguration;
import org.pcsoft.framework.jremote.ext.config.api.exception.JRemoteConfigurationException;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ClientXmlConfiguration extends RemoteXmlConfiguration<ClientConfigurationType> implements ClientConfiguration {
    public static ClientXmlConfiguration loadFrom(File file) throws IOException {
        try (final InputStream in = new FileInputStream(file)) {
            return loadFrom(in);
        }
    }

    public static ClientXmlConfiguration loadFrom(InputStream in) {
        final ClientConfigurationType type = JAXB.unmarshal(in, ClientConfigurationType.class);
        return loadFrom(type);
    }

    public static ClientXmlConfiguration loadFrom(ClientConfigurationType type) {
        final ClientXmlConfiguration configuration = new ClientXmlConfiguration();
        configuration.load(type);

        return configuration;
    }

    private int ownPort = 0;

    private ClientXmlConfiguration() {
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
    protected void load(ClientConfigurationType type) throws JRemoteConfigurationException {
        super.load(type);
        ownPort = type.getOwnPort();
    }
}
