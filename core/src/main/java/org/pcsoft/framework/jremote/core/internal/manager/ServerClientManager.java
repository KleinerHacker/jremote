package org.pcsoft.framework.jremote.core.internal.manager;

import org.pcsoft.framework.jremote.io.api.Client;
import org.pcsoft.framework.jremote.io.api.IoPlugin;
import org.pcsoft.framework.jremote.io.api.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ServiceLoader;

public final class ServerClientManager {
    private static final ServerClientManager instance = new ServerClientManager();

    public static ServerClientManager getInstance() {
        return instance;
    }

    private final Class<? extends Service> serviceClass;
    private final Class<? extends Client> clientClass;

    private ServerClientManager() {
        final IoPlugin ioPlugin = ServiceLoader.load(IoPlugin.class).findFirst().orElse(null);
        if (ioPlugin == null)
            throw new IllegalStateException("Unable to find any implementation for the IO API");

        serviceClass = ioPlugin.getServiceClass();
        clientClass = ioPlugin.getClientClass();
    }

    public Service createService() {
        try {
            return serviceClass.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException("Reflection error", e);
        }
    }

    public Client createClient() {
        try {
            return clientClass.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException("Reflection error", e);
        }
    }
}
