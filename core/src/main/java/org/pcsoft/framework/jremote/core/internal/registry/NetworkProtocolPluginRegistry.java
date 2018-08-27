package org.pcsoft.framework.jremote.core.internal.registry;

import org.pcsoft.framework.jremote.core.internal.validation.Validator;
import org.pcsoft.framework.jremote.np.api.Client;
import org.pcsoft.framework.jremote.np.api.Service;
import org.pcsoft.framework.jremote.np.api.NetworkProtocolPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ServiceLoader;

/**
 * Registry for a network protocol plugin (implementation of network protocol API, see {@link NetworkProtocolPlugin})
 */
public final class NetworkProtocolPluginRegistry extends PluginRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkProtocolPluginRegistry.class);
    private static final NetworkProtocolPluginRegistry instance = new NetworkProtocolPluginRegistry();

    public static NetworkProtocolPluginRegistry getInstance() {
        return instance;
    }

    private final Class<? extends Service> serviceClass;
    private final Class<? extends Client> clientClass;

    private final Class<?> registrationServiceClass;
    private final Class<?> keepAliveServiceClass;

    private NetworkProtocolPluginRegistry() {
        LOGGER.info("Start loading server client plugins");

        LOGGER.debug("> Check for found plugins in classpath / module path");
        if (ServiceLoader.load(NetworkProtocolPlugin.class).stream().count() > 1)
            throw new IllegalStateException("There are more than one JRemote service client plugin implementations in classpath / module path");

        final NetworkProtocolPlugin networkProtocolPlugin = ServiceLoader.load(NetworkProtocolPlugin.class).findFirst().orElse(null);
        if (networkProtocolPlugin == null)
            throw new IllegalStateException("Unable to find any JRemote service client plugin implementation for the Network Protocol API");
        LOGGER.debug("> Found server client plugin is: " + networkProtocolPlugin.getClass().getName());

        serviceClass = networkProtocolPlugin.getServiceClass();
        clientClass = networkProtocolPlugin.getClientClass();

        registrationServiceClass = networkProtocolPlugin.getRegistrationServiceClass();
        Validator.validateForRemoteService(registrationServiceClass);
        Validator.validateForRemoteRegistrationService(registrationServiceClass);

        keepAliveServiceClass = networkProtocolPlugin.getKeepAliveServiceClass();
        Validator.validateForRemoteService(keepAliveServiceClass);
        Validator.validateForRemoteKeepAliveService(keepAliveServiceClass);
    }

    /**
     * Creates a service instance of network protocol
     * @return
     */
    public Service createService() {
        LOGGER.trace("> Create service from server client plugin");
        try {
            return serviceClass.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException("Reflection error", e);
        }
    }

    /**
     * Creates a client instance of network protocol
     * @return
     */
    public Client createClient() {
        LOGGER.trace("> Create client from server client plugin");
        try {
            return clientClass.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException("Reflection error", e);
        }
    }

    /**
     * Returns the registration interface class for registration service, see {@link org.pcsoft.framework.jremote.np.api.annotation.RemoteRegistrationService}
     * @return
     */
    public Class<?> getRegistrationServiceClass() {
        return registrationServiceClass;
    }

    /**
     * Returns the keep alive interface class for keep alive service, see {@link org.pcsoft.framework.jremote.np.api.annotation.RemoteKeepAliveService}
     * @return
     */
    public Class<?> getKeepAliveServiceClass() {
        return keepAliveServiceClass;
    }
}
