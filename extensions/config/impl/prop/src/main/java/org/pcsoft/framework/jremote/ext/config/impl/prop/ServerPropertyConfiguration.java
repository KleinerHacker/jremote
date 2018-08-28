package org.pcsoft.framework.jremote.ext.config.impl.prop;

import org.pcsoft.framework.jremote.ext.config.api.ServerConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ServerPropertyConfiguration extends RemotePropertyConfiguration implements ServerConfiguration {
    public static ServerPropertyConfiguration loadFrom(File file) throws IOException {
        try (final InputStream in = new FileInputStream(file)) {
            return loadFrom(in);
        }
    }

    public static ServerPropertyConfiguration loadFrom(InputStream in) throws IOException {
        final Properties properties = new Properties();
        properties.load(in);

        return loadFrom(properties);
    }

    public static ServerPropertyConfiguration loadFrom(Properties properties) {
        final ServerPropertyConfiguration configuration = new ServerPropertyConfiguration();
        configuration.load(properties);

        return configuration;
    }

    private ServerPropertyConfiguration() {
    }
}
