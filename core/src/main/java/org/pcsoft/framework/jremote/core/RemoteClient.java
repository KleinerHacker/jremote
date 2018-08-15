package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.core.internal.manager.ClientProxyManager;
import org.pcsoft.framework.jremote.core.internal.processor.KeepAliveProcessor;
import org.pcsoft.framework.jremote.core.internal.registry.ServerClientPluginRegistry;
import org.pcsoft.framework.jremote.sc.api.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public final class RemoteClient implements Remote {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteClient.class);

    private final UUID uuid = UUID.randomUUID();
    private final String host;
    private final int port;

    private final ClientProxyManager proxyManager;
    private final DataManager dataManager = new DataManager();
    private final ControlManager controlManager = new ControlManager();

    private final List<Service> pushServiceList = new ArrayList<>();
    private final KeepAliveProcessor keepAliveProcessor = new KeepAliveProcessor();

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

    public UUID getUuid() {
        return uuid;
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
        startKeepAlive();
    }

    @Override
    public void close() throws Exception {
        LOGGER.info("Close Remote Client on " + host + ":" + port);

        stopKeepAlive();
        closeAndRemovePushServices();
    }

    @Override
    public ConnectionState getState() {
        return keepAliveProcessor.getConnectionState();
    }

    @Override
    public void addStateChangeListener(Consumer<ConnectionState> l) {
        keepAliveProcessor.addStateChangeListener(l);
    }

    @Override
    public void removeStateChangeListener(Consumer<ConnectionState> l) {
        keepAliveProcessor.removeStateChangeListener(l);
    }

    //region Helper Methods
    private void startKeepAlive() {
        LOGGER.debug("> Start keep alive thread");

        keepAliveProcessor.setup(getProxyManager().getRemoteRegistrationClient(), getProxyManager().getRemoteKeepAliveClient(), this);
        keepAliveProcessor.start();
    }

    private void stopKeepAlive() {
        LOGGER.debug("> Stop keep alive thread");

        if (keepAliveProcessor.isRunning()) {
            keepAliveProcessor.stop();
        }
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
    //endregion

    //region Helper Classes
    public final class DataManager {
        private DataManager() {
        }

        public <T> T getRemoteModel(Class<T> clazz) {
            return proxyManager.getRemoteModelProxy(clazz);
        }

        public <T> T getRemoteObserver(Class<T> clazz) {
            return proxyManager.getRemoteObserverProxy(clazz);
        }
    }

    public final class ControlManager {
        private ControlManager() {
        }

        public <T> T getControlClient(Class<T> clazz) {
            return proxyManager.getRemoteControlClientProxy(clazz);
        }
    }
    //endregion
}
