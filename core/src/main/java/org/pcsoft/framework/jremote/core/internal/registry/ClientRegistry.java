package org.pcsoft.framework.jremote.core.internal.registry;

import org.pcsoft.framework.jremote.core.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public final class ClientRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRegistry.class);

    private final Map<String, Client> clientMap = new HashMap<>();
    private final List<BiConsumer<String, Integer>> clientRegisteredListenerList = new ArrayList<>();
    private final List<BiConsumer<String, Integer>> clientUnregisteredListenerList = new ArrayList<>();

    public void registerClient(String uuid, String host, int port) {
        LOGGER.info("> Register new client <" + uuid + "> from " + host + ":" + port);
        synchronized (clientMap) {
            clientMap.put(uuid, new Client(host, port));
            fireClientRegistered(host, port);
        }
    }

    public void unregisterClient(String uuid) {
        LOGGER.info("> Unregister new client <" + uuid + ">");
        synchronized (clientMap) {
            final Client client = clientMap.get(uuid);

            clientMap.remove(uuid);
            fireClientUnregistered(client.getHost(), client.getPort());
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

    public void addClientRegisteredListener(BiConsumer<String, Integer> l) {
        clientRegisteredListenerList.add(l);
    }

    public void removeClientRegisteredListener(BiConsumer<String, Integer> l) {
        clientRegisteredListenerList.add(l);
    }

    public void addClientUnregisteredListener(BiConsumer<String, Integer> l) {
        clientUnregisteredListenerList.add(l);
    }

    public void removeClientUnregisteredListener(BiConsumer<String, Integer> l) {
        clientUnregisteredListenerList.add(l);
    }

    private void fireClientRegistered(String host, int port) {
        for (final BiConsumer<String, Integer> l : clientRegisteredListenerList) {
            l.accept(host, port);
        }
    }

    private void fireClientUnregistered(String host, int port) {
        for (final BiConsumer<String, Integer> l : clientUnregisteredListenerList) {
            l.accept(host, port);
        }
    }
}
