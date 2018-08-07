package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.core.internal.manager.ClientProxyManager;
import org.pcsoft.framework.jremote.core.internal.registry.ServerClientPluginRegistry;
import org.pcsoft.framework.jremote.io.api.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class RemoteClient implements Remote {
    private final String host;
    private final int port;

    private final ClientProxyManager proxyManager;
    private final DataManager dataManager = new DataManager();
    private final ControlManager controlManager = new ControlManager();

    private final List<Service> pushServiceList = new ArrayList<>();

    RemoteClient(String host, int port) {
        this.host = host;
        this.port = port;

        this.proxyManager = new ClientProxyManager();
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
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void open() throws IOException {
        createAndOpenPushServices();
    }

    @Override
    public void close() throws Exception {
        closeAndRemovePushServices();
    }

    private void createAndOpenPushServices() throws IOException {
        for (final Class<?> pushClass : proxyManager.getRemotePushClasses()) {
            final Object pushServiceProxy = proxyManager.getRemotePushServiceProxy(pushClass);

            final Service service = ServerClientPluginRegistry.getInstance().createService();
            service.setServiceImplementation(pushServiceProxy);
            service.open(host, port);

            pushServiceList.add(service);
        }
    }

    private void closeAndRemovePushServices() throws IOException {
        for (final Service service : pushServiceList) {
            service.close();
        }
        pushServiceList.clear();
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
