package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.core.internal.manager.ClientProxyManager;

public final class RemoteClient implements Remote {
    private final ClientProxyManager proxyManager;

    RemoteClient() {
        proxyManager = new ClientProxyManager();
    }

    ClientProxyManager getProxyManager() {
        return proxyManager;
    }
}
