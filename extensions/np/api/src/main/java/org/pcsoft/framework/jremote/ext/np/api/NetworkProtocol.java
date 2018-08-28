package org.pcsoft.framework.jremote.ext.np.api;

/**
 * Interface for network protocol mechanism
 */
public interface NetworkProtocol {
    /**
     * Creates a new service for this protocol to use.
     * @param serviceImplementation Implementation of the class to invoke methods via network
     * @return
     */
    Service createService(Object serviceImplementation);

    /**
     * Creates a new client for this protocol to use.
     * @param serviceClass Class of the service to create client for
     * @return
     */
    Client createClient(Class<?> serviceClass);

    /**
     * Returns the concrete remote registration service class to use for this plugin
     *
     * @return
     */
    Class<?> getRegistrationServiceClass();

    /**
     * Returns the concrete remote keep alive service class to use for this plugin
     *
     * @return
     */
    Class<?> getKeepAliveServiceClass();
}
