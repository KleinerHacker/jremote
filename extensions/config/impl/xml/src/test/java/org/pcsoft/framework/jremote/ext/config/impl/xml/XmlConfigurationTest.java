package org.pcsoft.framework.jremote.ext.config.impl.xml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pcsoft.framework.jremote.ext.config.api.exception.JRemoteConfigurationException;

import java.io.IOException;

class XmlConfigurationTest {

    @Test
    void testServerLoading() throws IOException {
        final ServerXmlConfiguration configuration =
                ServerXmlConfiguration.loadFrom(getClass().getResourceAsStream("/server.xml"));
        try {
            configuration.validate();
        } catch (JRemoteConfigurationException e) {
            Assertions.fail(e);
        }

        Assertions.assertEquals("www.server.com", configuration.getHost());
        Assertions.assertEquals(4321, configuration.getPort());
    }

    @Test
    void testClientLoading() throws IOException {
        final ClientXmlConfiguration configuration =
                ClientXmlConfiguration.loadFrom(getClass().getResourceAsStream("/client.xml"));
        try {
            configuration.validate();
        } catch (JRemoteConfigurationException e) {
            Assertions.fail(e);
        }

        Assertions.assertEquals("www.client.com", configuration.getHost());
        Assertions.assertEquals(9876, configuration.getPort());
        Assertions.assertEquals(6789, configuration.getOwnPort());
    }

}