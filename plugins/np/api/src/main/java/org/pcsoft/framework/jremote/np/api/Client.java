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

    /**
     * Returns the service class the client work for. All methods in {@link #invokeRemote(Method, Object...)} are part of this interface class
     *
     * @return
     */
    Class<?> getServiceClass();

    /**
     * Setup the service class the client work for. See {@link #getServiceClass()}
     *
     * @param serviceClass
     */
    void setServiceClass(Class<?> serviceClass);
}
