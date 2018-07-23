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

    public <T>T getRemoteModel(Class<T> clazz) {
        return proxyManager.geRemoteModelProxy(clazz);
    }

    @Override
    public void open() {

    }

    @Override
    public void close() throws Exception {

    }
}
