package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.core.internal.manager.ClientProxyManager;
import org.pcsoft.framework.jremote.core.internal.registry.ServerClientPluginRegistry;
import org.pcsoft.framework.jremote.io.api.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class RemoteClient implements Remote {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteClient.class);

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
        LOGGER.info("Open Remote Client on " + host + ":" + port);
        createAndOpenPushServices();
    }

    @Override
    public void close() throws Exception {
        LOGGER.info("Close Remote Client on " + host + ":" + port);
        closeAndRemovePushServices();
    }

    private void createAndOpenPushServices() throws IOException {
        LOGGER.debug("> Create and open push services");
        for (final Class<?> pushClass : proxyManager.getRemotePushClasses()) {
            final Object pushServiceProxy = proxyManager.getRemotePushServiceProxy(pushClass);

            final Service service = ServerClientPluginRegistry.getInstance().createService();
            service.setServiceImplementation(pushServiceProxy);
            service.open(host, port);

            pushServiceList.add(service);
        }
    }

    private void closeAndRemovePushServices() throws IOException {
        LOGGER.debug("> Close and remove push services");
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
