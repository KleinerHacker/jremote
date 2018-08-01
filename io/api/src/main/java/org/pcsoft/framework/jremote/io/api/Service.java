package org.pcsoft.framework.jremote.io.api;

import java.io.IOException;

/**
 * Represent the base interface for a service implementation
 */
public interface Service {
    /**
     * Start service on given host and port
     * @param host
     * @param port
     * @throws IOException
     */
    void startService(String host, int port) throws IOException;

    /**
     * Stop service
     * @throws IOException
     */
    void stopService() throws IOException;

    /**
     * Setup the service implementation to call methods on
     * @param serviceImplementation
     */
    void setServiceImplementation(Object serviceImplementation);

    /**
     * Returns the service implementation, see {@link #setServiceImplementation(Object)}
     * @return
     */
    Object getServiceImplementation();
}
