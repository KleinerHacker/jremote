package org.pcsoft.framework.jremote.ext.config.api;

/**
 * Special configuration interface for the client side
 */
public interface ClientConfiguration extends RemoteConfiguration {
    /**
     * Returns the own port (for client services)
     * @return
     */
    int getOwnPort();
}
