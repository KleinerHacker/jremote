package org.pcsoft.framework.jremote.core.internal.registry;

import org.pcsoft.framework.jremote.core.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class ClientRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRegistry.class);

    private final Map<String, Client> clientMap = new HashMap<>();
    private final List<Consumer<Client>> clientRegisteredListenerList = new ArrayList<>();
    private final List<Consumer<Client>> clientUnregisteredListenerList = new ArrayList<>();

    public void registerClient(String uuid, String host, int port) {
        LOGGER.info("> Register new client <" + uuid + "> from " + host + ":" + port);
        synchronized (clientMap) {
            final Client client = new Client(host, port);
            clientMap.put(uuid, client);
            fireClientRegistered(client);
        }
    }

    public void unregisterClient(String uuid) {
        LOGGER.info("> Unregister client <" + uuid + ">");
        synchronized (clientMap) {
            final Client client = clientMap.get(uuid);

            clientMap.remove(uuid);
            fireClientUnregistered(client);
        }
    }

    public Client[] getClients() {
        synchronized (clientMap) {
            return clientMap.values().toArray(new Client[0]);
        }
    }

    public boolean containsClient(String uuid) {
        synchronized (clientMap) {
            return clientMap.containsKey(uuid);
        }
    }

    public void addClientRegisteredListener(Consumer<Client> l) {
        clientRegisteredListenerList.add(l);
    }

    public void removeClientRegisteredListener(Consumer<Client> l) {
        clientRegisteredListenerList.add(l);
    }

    public void addClientUnregisteredListener(Consumer<Client> l) {
        clientUnregisteredListenerList.add(l);
    }

    public void removeClientUnregisteredListener(Consumer<Client> l) {
        clientUnregisteredListenerList.add(l);
    }

    private void fireClientRegistered(Client client) {
        for (final Consumer<Client> l : clientRegisteredListenerList) {
            l.accept(client);
        }
    }

    private void fireClientUnregistered(Client client) {
        for (final Consumer<Client> l : clientUnregisteredListenerList) {
            l.accept(client);
        }
    }
}
