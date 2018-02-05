package org.pcsoft.framework.jremote.api.config;

import java.net.URI;

/**
 * JRemote configuration for client
 */
public final class JRemoteClientConfiguration extends JRemoteConfiguration {
    private URI remoteUri = URI.create("http://localhost/" + JRemoteServerConfiguration.class.getSimpleName());

    /**
     * Remote URI to the {@link java.rmi.server.RemoteServer}
     * @return
     */
    public URI getRemoteUri() {
        return remoteUri;
    }

    public void setRemoteUri(URI remoteUri) {
        this.remoteUri = remoteUri;
    }
}
