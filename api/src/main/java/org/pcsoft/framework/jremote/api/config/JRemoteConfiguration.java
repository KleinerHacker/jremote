package org.pcsoft.framework.jremote.api.config;

import java.net.URI;

/**
 * Basic implementation for a JRemote configuration
 */
public abstract class JRemoteConfiguration {
    private URI localUri = URI.create("http://localhost/" + getClass().getSimpleName());
    private boolean checkInterfaceCompatibility = true;

    /**
     * Local URI for own services
     * @return
     */
    public URI getLocalUri() {
        return localUri;
    }

    public void setLocalUri(URI localUri) {
        this.localUri = localUri;
    }

    /**
     * Activate oder deactivate the check on first connection of interface compatibility. <b>Should be used always!</b>
     * @return
     */
    public boolean isCheckInterfaceCompatibility() {
        return checkInterfaceCompatibility;
    }

    public void setCheckInterfaceCompatibility(boolean checkInterfaceCompatibility) {
        this.checkInterfaceCompatibility = checkInterfaceCompatibility;
    }
}
