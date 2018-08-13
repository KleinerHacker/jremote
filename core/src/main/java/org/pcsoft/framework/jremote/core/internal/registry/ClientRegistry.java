package org.pcsoft.framework.jremote.core.internal.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public final class ClientRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRegistry.class);

    private final Map<String, Client> clientMap = new HashMap<>();

    public void registerClient(String uuid, String host, int port) {
        LOGGER.info("> Register new client <" + uuid + "> from " + host + ":" + port);
        synchronized (clientMap) {
            clientMap.put(uuid, new Client(host, port));
        }
    }

    public void unregisterClient(String uuid) {
        LOGGER.info("> Unregister new client <" + uuid + ">");
        synchronized (clientMap) {
            clientMap.remove(uuid);
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

    public static final class Client {
        private final String host;
        private final int port;

        private Client(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }
    }
}
