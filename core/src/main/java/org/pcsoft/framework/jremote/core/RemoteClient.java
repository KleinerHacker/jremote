package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.core.internal.manager.ClientProxyManager;

public final class RemoteClient implements Remote {
    private final ClientProxyManager proxyManager;
    private final DataManager dataManager = new DataManager();
    private final ControlManager controlManager = new ControlManager();

    RemoteClient() {
        proxyManager = new ClientProxyManager();
    }

    ClientProxyManager getProxyManager() {
        return proxyManager;
    }

    public DataManager getData() {
        return dataManager;
    }

    public ControlManager getControl() {
        return controlManager;
    }

    @Override
    public void open() {

    }

    @Override
    public void close() throws Exception {

    }

    public final class DataManager {
        private DataManager() {
        }

        public <T>T getRemoteModel(Class<T> clazz) {
            return proxyManager.getRemoteModelProxy(clazz);
        }

        public <T>T getRemoteObserver(Class<T> clazz) {
            return proxyManager.getRemoteObserverProxy(clazz);
        }
    }

    public final class ControlManager {
        private ControlManager() {
        }
    }
}
