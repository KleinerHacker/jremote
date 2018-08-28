package org.pcsoft.framework.jremote.ext.config.api;

import org.pcsoft.framework.jremote.ext.config.api.exception.JRemoteConfigurationException;

/**
 * Remote configuration interface for all extension implementations for configuration handling
 */
public interface RemoteConfiguration {
    /**
     * Returns the configured host
     * @return
     */
    String getHost();
    /**
     * Returns the configured port
     * @return
     */
    int getPort();

    /**
     * Validate the configuration and check that all needed values are set
     * @throws JRemoteConfigurationException Is thrown if validation failed
     */
    void validate() throws JRemoteConfigurationException;
}
