package org.pcsoft.framework.jremote.core.internal;

import org.pcsoft.framework.jremote.api.factory.ClientFactory;
import org.pcsoft.framework.jremote.api.type.ClientConnection;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public final class ClientRegister {
    private final ClientFactory clientFactory;
    private final Map<String, ClientConnection> clientMap = new HashMap<>();

    public ClientRegister(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    public void registerClient(final String address) {
        synchronized (clientMap) {
            if (clientMap.containsKey(address))
                return;

            clientMap.put(address, clientFactory.createClientConnection(URI.create(address)));
        }
    }

    public void unregisterClient(final String address) {
        synchronized (clientMap) {
            if (!clientMap.containsKey(address))
                return;

            clientMap.remove(address);
        }
    }

    public boolean isClientRegistered(final String address) {
        synchronized (clientMap) {
            return clientMap.containsKey(address);
        }
    }

    public ClientConnection getClient(final String address) {
        synchronized (clientMap) {
            return clientMap.get(address);
        }
    }

    public String[] getClientAddresses() {
        synchronized (clientMap) {
            return clientMap.keySet().toArray(new String[clientMap.keySet().size()]);
        }
    }
}
