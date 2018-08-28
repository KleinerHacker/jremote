package org.pcsoft.framework.jremote.np.api;

import java.io.Closeable;
import java.io.IOException;

/**
 * Represent the base interface for a service implementation
 */
public interface Service extends Closeable {
    /**
     * Start service on given host and port
     *
     * @param host
     * @param port
     * @throws IOException
     */
    void open(String host, int port) throws IOException;
}
