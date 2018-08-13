package org.pcsoft.framework.jremote.core.internal.registry;

import org.pcsoft.framework.jremote.io.api.Client;
import org.pcsoft.framework.jremote.io.api.ServiceClientPlugin;
import org.pcsoft.framework.jremote.io.api.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ServiceLoader;

public final class ServerClientPluginRegistry extends PluginRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerClientPluginRegistry.class);
    private static final ServerClientPluginRegistry instance = new ServerClientPluginRegistry();

    public static ServerClientPluginRegistry getInstance() {
        return instance;
    }

    private final Class<? extends Service> serviceClass;
    private final Class<? extends Client> clientClass;

    private final Class<?> registrationServiceClass;
    private final Class<?> keepAliveServiceClass;

    private ServerClientPluginRegistry() {
        LOGGER.info("Start loading server client plugins");

        LOGGER.debug("> Check for found plugins in classpath / module path");
        if (ServiceLoader.load(ServiceClientPlugin.class).stream().count() > 1)
            throw new IllegalStateException("There are more than one jremote service client plugin implementations in classpath / module path");

        final ServiceClientPlugin serviceClientPlugin = ServiceLoader.load(ServiceClientPlugin.class).findFirst().orElse(null);
        if (serviceClientPlugin == null)
            throw new IllegalStateException("Unable to find any jremote service client plugin implementation for the IO API");
        LOGGER.debug("> Found server client plugin is: " + serviceClientPlugin.getClass().getName());

        serviceClass = serviceClientPlugin.getServiceClass();
        clientClass = serviceClientPlugin.getClientClass();

        //TODO: Validate
        registrationServiceClass = serviceClientPlugin.getRegistrationServiceClass();
        keepAliveServiceClass = serviceClientPlugin.getKeepAliveServiceClass();
    }

    public Service createService() {
        LOGGER.trace("> Create service from server client plugin");
        try {
            return serviceClass.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException("Reflection error", e);
        }
    }

    public Client createClient() {
        LOGGER.trace("> Create client from server client plugin");
        try {
            return clientClass.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException("Reflection error", e);
        }
    }

    public Class<?> getRegistrationServiceClass() {
        return registrationServiceClass;
    }

    public Class<?> getKeepAliveServiceClass() {
        return keepAliveServiceClass;
    }
}
