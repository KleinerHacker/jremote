package org.pcsoft.framework.jremote.core.internal.registry;

import java.util.HashMap;
import java.util.Map;

public final class ClientRegistry {
    private final Map<String, Client> clientMap = new HashMap<>();

    public void registerClient(String uuid, String host, int port) {
        clientMap.put(uuid, new Client(host, port));
    }

    public void unregisterClient(String uuid) {
        clientMap.remove(uuid);
    }

    public Client[] getClients() {
        return clientMap.values().toArray(new Client[0]);
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
