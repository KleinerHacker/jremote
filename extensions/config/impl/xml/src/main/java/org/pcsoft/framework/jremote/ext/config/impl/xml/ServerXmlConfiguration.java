package org.pcsoft.framework.jremote.ext.config.impl.xml;

import org.pcsoft.framework.jremote.ext.config.api.ServerConfiguration;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ServerXmlConfiguration extends RemoteXmlConfiguration<ServerConfigurationType> implements ServerConfiguration {
    public static ServerXmlConfiguration loadFrom(File file) throws IOException {
        try (final InputStream in = new FileInputStream(file)) {
            return loadFrom(in);
        }
    }

    public static ServerXmlConfiguration loadFrom(InputStream in) {
        final ServerConfigurationType type = JAXB.unmarshal(in, ServerConfigurationType.class);
        return loadFrom(type);
    }

    public static ServerXmlConfiguration loadFrom(ServerConfigurationType type) {
        final ServerXmlConfiguration configuration = new ServerXmlConfiguration();
        configuration.load(type);

        return configuration;
    }

    private ServerXmlConfiguration() {
    }
}
