package org.pcsoft.framework.jremote.np.api;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Interface for a client based implementation
 */
public interface Client extends Closeable {
    /**
     * Open the client on given host / port
     *
     * @param host
     * @param port
     * @throws IOException
     */
    void open(String host, int port) throws IOException;

    /**
     * Invoke the given method on server with given parameter values
     *
     * @param method
     * @param params
     * @return
     * @throws IOException
     */
    Object invokeRemote(Method method, Object... params) throws IOException;
}
