package org.pcsoft.framework.jremote.np.api;

/**
 * Interface for network protocol plugin mechanism
 */
public interface NetworkProtocolPlugin {
    /**
     * Returns the class of {@link Service} for this plugin. Class is instantiate automatically.
     *
     * @return
     */
    Class<? extends Service> getServiceClass();

    /**
     * Returns the class of {@link Client} for this plugin. Class is instantiate automatically.
     *
     * @return
     */
    Class<? extends org.pcsoft.framework.jremote.np.api.Client> getClientClass();

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
